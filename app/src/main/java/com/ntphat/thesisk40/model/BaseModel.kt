package com.ntphat.thesisk40.model

import com.ntphat.thesisk40.builder.ApiRequestBuilder
import com.ntphat.thesisk40.presenter.Presenter
import okhttp3.OkHttpClient

open class BaseModel {
    protected val apiRequestBuilder: ApiRequestBuilder = ApiRequestBuilder()
    protected val okHttpClient: OkHttpClient = OkHttpClient()
}