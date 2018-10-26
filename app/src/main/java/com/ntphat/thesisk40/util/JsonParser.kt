package com.ntphat.thesisk40.util

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.ntphat.thesisk40.data.Response

class JsonParser {
    companion object {
        inline fun <reified T> fromJson(json: String): T? {
            val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
            return try {
                gson.fromJson(json, T::class.java)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        inline fun <reified T> toJson(entity: T): String {
            val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
            return gson.toJson(entity)
        }
    }
}