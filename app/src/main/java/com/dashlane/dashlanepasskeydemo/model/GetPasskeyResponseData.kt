package com.dashlane.dashlanepasskeydemo.model

import com.google.gson.annotations.SerializedName

data class GetPasskeyResponseData(
    @SerializedName("response") val response: Response,
    @SerializedName("authenticatorAttachment") val authenticatorAttachment: String,
    @SerializedName("id") val id: String,
    @SerializedName("rawId") val rawId: String,
    @SerializedName("type") val type: String
) {
    data class Response(
        @SerializedName("clientDataJSON") val clientDataJSON: String,
        @SerializedName("authenticatorData") val authenticatorData: String,
        @SerializedName("signature") val signature: String,
        @SerializedName("userHandle") val userHandle: String
    )
}