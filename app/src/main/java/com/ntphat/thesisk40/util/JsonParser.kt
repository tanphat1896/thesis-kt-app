package com.ntphat.thesisk40.util

import com.google.gson.Gson
import com.ntphat.thesisk40.data.Response

class JsonParser {
    companion object {
        inline fun <reified T> fromJson(json: String): T? {
            return Gson().fromJson(json, T::class.java)
        }
    }
}