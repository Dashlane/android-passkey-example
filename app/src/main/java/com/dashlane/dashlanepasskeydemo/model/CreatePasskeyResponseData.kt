package com.dashlane.dashlanepasskeydemo.model

import com.google.gson.annotations.SerializedName

data class CreatePasskeyResponseData(
    @SerializedName("response") val response: Response,
    @SerializedName("authenticatorAttachment") val authenticatorAttachment: String,
    @SerializedName("id") val id: String,
    @SerializedName("rawId") val rawId: String,
    @SerializedName("type") val type: String
) {
    data class Response(
        @SerializedName("clientDataJSON") val clientDataJSON: String,
        @SerializedName("attestationObject") val attestationObject: String,
        @SerializedName("transports") val transports: List<String>
    )
}