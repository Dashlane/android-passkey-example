package com.dashlane.dashlanepasskeydemo.model

data class CreatePasskeyRequest(
    val challenge: String,
    val rp: Rp,
    val user: User,
    val pubKeyCredParams: List<PubKeyCredParams>,
    val timeout: Long,
    val attestation: String,
    val excludeCredentials: List<Any>,
    val authenticatorSelection: AuthenticatorSelection
) {
    data class Rp(
        val name: String,
        val id: String
    )

    data class User(
        val id: String,
        val name: String,
        val displayName: String
    )

    data class PubKeyCredParams(
        val type: String,
        val alg: Int
    )

    data class AuthenticatorSelection(
        val authenticatorAttachment: String,
        val requireResidentKey: Boolean,
        val residentKey: String,
        val userVerification: String
    )
}