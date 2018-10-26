package com.ntphat.thesisk40.model

import com.ntphat.thesisk40.builder.ApiRequestBuilder
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

open class BaseModel {
    protected val apiRequestBuilder: ApiRequestBuilder = ApiRequestBuilder()
    protected val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
}