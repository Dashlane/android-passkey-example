package com.dashlane.dashlanepasskeydemo.repository

import android.content.SharedPreferences
import com.dashlane.dashlanepasskeydemo.b64Encode
import com.dashlane.dashlanepasskeydemo.model.UserData
import com.google.gson.Gson
import java.security.SecureRandom
import javax.inject.Inject

interface AccountRepository {
    fun saveUserAccount(userId: String, userData: UserData)
    fun getUserAccount(userId: String): UserData?
    fun searchUserWithEmail(email: String): UserData?
    fun getCreatePasskeyRequest(userId: String, email: String): String
    fun getLoginPasskeyRequest(allowedCredential: List<String> = emptyList()): String
}

class AccountRepositoryLocal @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : AccountRepository {

    /**
     * Save the user account to the local database
     */
    override fun saveUserAccount(userId: String, userData: UserData) {
        with(sharedPreferences.edit()) {
            putString(userId, gson.toJson(userData))
            apply()
        }
    }

    /**
     * Retrieve the user account from the user id inside the local database
     */
    override fun getUserAccount(userId: String): UserData? {
        return gson.fromJson(sharedPreferences.getString(userId, null), UserData::class.java)
    }

    /**
     * Search if this email is already registered in the local database
     */
    override fun searchUserWithEmail(email: String): UserData? {
        val allUsers = sharedPreferences.all
        // Find user with the email that exists in the local database
        return allUsers.values
            .map { gson.fromJson(it as String, UserData::class.java) }
            .find {
                it.email == email
            }
    }

    /**
     * Create the request to create a passkey. From https://w3c.github.io/webauthn/#sctn-sample-registration
     */
    override fun getCreatePasskeyRequest(userId: String, email: String): String {
        return "{\n" +
                "  \"challenge\":\"${generateFidoChallenge()}\",\n" +
                "  \"rp\":{\n" +
                "    \"name\":\"Dashlane Passkey Demo\",\n" +
                "    \"id\":\"$RELYING_PARTY_ID\"\n" +
                "  },\n" +
                "  \"user\":{\n" +
                "    \"id\":\"$userId\",\n" +
                "    \"name\":\"$email\",\n" +
                "    \"displayName\":\"$email\"\n" +
                "  },\n" +
                "  \"pubKeyCredParams\":[\n" +
                "    {\"type\":\"public-key\",\"alg\":-7}],\n" +
                "  \"timeout\":1800000,\n" +
                "  \"attestation\":\"none\",\n" +
                "  \"excludeCredentials\":[],\n" +
                "  \"authenticatorSelection\":{\n" +
                "    \"authenticatorAttachment\":\"platform\",\n" +
                "    \"requireResidentKey\":true,\n" +
                "    \"residentKey\": \"required\",\n" +
                "    \"userVerification\":\"required\"\n" +
                "  }\n" +
                "}"
    }

    /**
     * Create the request to login with a passkey. From https://w3c.github.io/webauthn/#sctn-sample-authentication
     */
    override fun getLoginPasskeyRequest(allowedCredential: List<String>): String {
        return "{\n" +
                "  \"challenge\":\"${generateFidoChallenge()}\",\n" +
                "  \"allowCredentials\":$allowedCredential,\n" +
                "  \"timeout\":1800000,\n" +
                "  \"userVerification\":\"required\",\n" +
                "  \"rpId\":\"$RELYING_PARTY_ID\"\n" +
                "}"
    }

    /**
     * Generates a random challenge for the FIDO request, that should be signed by the authenticator
     */
    private fun generateFidoChallenge(): String {
        val secureRandom = SecureRandom()
        val challengeBytes = ByteArray(32)
        secureRandom.nextBytes(challengeBytes)
        return challengeBytes.b64Encode()
    }

    companion object {
        private const val RELYING_PARTY_ID = "dashlanepasskeydemo.com"
    }
}