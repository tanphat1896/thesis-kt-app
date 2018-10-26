package com.ntphat.thesisk40.constant

import okhttp3.MediaType

object Http {
    @JvmStatic
    val POST = "POST"
    @JvmStatic
    val GET = "GET"
    @JvmStatic
    val PUT = "PUT"
    @JvmStatic
    val DELETE = "DELETE"

    @JvmStatic
    val APP_JSON = "application/json"

    @JvmStatic
    val MEDIA_JSON = MediaType.parse("application/json; charset=utf-8")

    @JvmStatic
    val MEDIA_JPEG = MediaType.parse("image/jpg")

    @JvmStatic
    val AUTH_HEADER = "Authorization"
}