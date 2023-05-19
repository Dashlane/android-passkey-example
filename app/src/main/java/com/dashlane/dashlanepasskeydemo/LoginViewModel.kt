package com.dashlane.dashlanepasskeydemo

import android.app.Activity
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.CreateCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nstant.`in`.cbor.CborDecoder
import co.nstant.`in`.cbor.model.ByteString
import co.nstant.`in`.cbor.model.Map
import co.nstant.`in`.cbor.model.NegativeInteger
import co.nstant.`in`.cbor.model.UnicodeString
import com.dashlane.dashlanepasskeydemo.model.CreatePasskeyResponseData
import com.dashlane.dashlanepasskeydemo.model.GetPasskeyResponseData
import com.dashlane.dashlanepasskeydemo.model.UserData
import com.dashlane.dashlanepasskeydemo.repository.AccountRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util
import org.bouncycastle.jce.ECNamedCurveTable
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.Signature
import java.security.spec.ECPoint
import java.security.spec.ECPublicKeySpec
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val credentialManager: CredentialManager,
    private val gson: Gson
) : ViewModel() {
    private val _state: MutableSharedFlow<LoginState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state: SharedFlow<LoginState> = _state.asSharedFlow()

    /**
     * Validate the email address and check if the account already exists in the local database
     */
    fun validateEmail(email: String, activity: Activity) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val userData = accountRepository.searchUserWithEmail(email)
            if (userData != null) {
                // Account already exist, we try to get the passkey from the credential manager
                loginWithPasskey(
                    activity,
                    GetPublicKeyCredentialOption(accountRepository.getLoginPasskeyRequest(listOf(userData.credentialId)))
                )
            } else {
                _state.tryEmit(LoginState.EmailSuccess(email))
            }
        } else {
            _state.tryEmit(LoginState.EmailError)
        }
    }

    /**
     * Make a request to create a passkey and save the user account to the local database
     */
    fun createPasskeyAccount(activity: Activity, email: String) {
        viewModelScope.launch {
            val userId = UUID.randomUUID().toString()
            try {
                val response = credentialManager.createCredential(
                    activity,
                    CreatePublicKeyCredentialRequest(accountRepository.getCreatePasskeyRequest(userId, email)),
                )
                val responseData = gson.fromJson(
                    (response as CreatePublicKeyCredentialResponse).registrationResponseJson,
                    CreatePasskeyResponseData::class.java
                )
                val attestationObject = CborDecoder.decode(responseData.response.attestationObject.b64Decode()).first()
                val authData = (attestationObject as Map).get(UnicodeString("authData")) as ByteString
                val publicKey = parseAuthData(authData.bytes)
                val userData = UserData(responseData.id, email, publicKey.b64Encode(), Instant.now().epochSecond)
                accountRepository.saveUserAccount(responseData.id, userData)
                _state.emit(LoginState.CreateAccountSuccess)
            } catch (e: CreateCredentialException) {
                _state.emit(LoginState.CreateAccountError(e.message ?: "Unknown error"))
                e.printStackTrace()
            }
        }
    }

    /**
     * Login to an existing account with a passkey. We try to get the user's data (public key, email, creation date...) from the userId
     * and verify the signature with the public key.
     */
    fun loginWithPasskey(
        activity: Activity,
        option: GetPublicKeyCredentialOption = GetPublicKeyCredentialOption(accountRepository.getLoginPasskeyRequest())
    ) {
        viewModelScope.launch {
            try {
                val responseData = getLoginResponse(activity, option)
                val userData = accountRepository.getUserAccount(responseData.id)
                if (userData == null) {
                    _state.emit(LoginState.LoginError("No account found for this user"))
                    return@launch
                }
                val publicKey = userData.publicKey.toJavaPublicKey()
                if (verifySignature(responseData, publicKey)) {
                    _state.emit(LoginState.LoginSuccess(userData.email, userData.creationDate))
                } else {
                    _state.emit(LoginState.LoginError("Signature verification failed"))
                }
            } catch (e: Exception) {
                _state.emit(LoginState.LoginError(e.message ?: "Unknown error"))
                e.printStackTrace()
            }
        }
    }

    /**
     * Call the credential manager to create a passkey login request
     */
    private suspend fun getLoginResponse(
        activity: Activity,
        option: GetPublicKeyCredentialOption
    ): GetPasskeyResponseData {
        val getCredRequest = GetCredentialRequest(listOf(option))
        val response = credentialManager.getCredential(activity, getCredRequest)
        val cred = response.credential as PublicKeyCredential
        return gson.fromJson(cred.authenticationResponseJson, GetPasskeyResponseData::class.java)
    }

    /**
     * Check if the signature is valid by signing the clientDataJSON with the public key
     */
    private fun verifySignature(responseData: GetPasskeyResponseData, publicKey: PublicKey): Boolean {
        val signature = responseData.response.signature.b64Decode()
        val sig = Signature.getInstance("SHA256withECDSA")
        sig.initVerify(publicKey)
        val md = MessageDigest.getInstance("SHA-256")
        val clientDataHash = md.digest(responseData.response.clientDataJSON.b64Decode())
        val signatureBase = responseData.response.authenticatorData.b64Decode() + clientDataHash
        sig.update(signatureBase)
        return sig.verify(signature)
    }

    /**
     * Convert the user's public key, stored as String, to a java PublicKey
     */
    private fun String.toJavaPublicKey(): PublicKey {
        val decoded = CborDecoder.decode(this.b64Decode()).first() as Map
        val publicKeyX = decoded[NegativeInteger(-2)] as ByteString
        val publicKeyY = decoded[NegativeInteger(-3)] as ByteString
        val ecPoint = ECPoint(BigInteger(1, publicKeyX.bytes), BigInteger(1, publicKeyY.bytes))
        val params = ECNamedCurveTable.getParameterSpec("secp256r1")
        val ellipticCurve = EC5Util.convertCurve(params.curve, params.seed)
        val params2 = EC5Util.convertSpec(ellipticCurve, params)
        val keySpec = ECPublicKeySpec(ecPoint, params2)
        return KeyFactory.getInstance("EC").generatePublic(keySpec)
    }

    /**
     * Parse the authData from the attestationObject to get the public key
     */
    private fun parseAuthData(buffer: ByteArray): ByteArray {
        /*val rpIdHash = buffer.copyOfRange(0, 32)
        val flags = buffer.copyOfRange(32, 33)
        val signCount = buffer.copyOfRange(33, 37)
        val aaguid = buffer.copyOfRange(37, 53)*/
        val credentialIdLength = buffer.copyOfRange(53, 55)
        //val credentialId = buffer.copyOfRange(55, 55 + credentialIdLength[1].toInt())
        return buffer.copyOfRange(55 + credentialIdLength[1].toInt(), buffer.size)
    }

    /**
     * Disconnect a user
     */
    fun disconnect() {
        _state.tryEmit(LoginState.Disconnected)
    }
}