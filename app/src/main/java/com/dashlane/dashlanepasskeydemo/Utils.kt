package com.dashlane.dashlanepasskeydemo

import android.util.Base64

fun String.b64Decode(): ByteArray {
    return Base64.decode(this, Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE)
}

fun ByteArray.b64Encode(): String {
    return Base64.encodeToString(this, Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE)
}