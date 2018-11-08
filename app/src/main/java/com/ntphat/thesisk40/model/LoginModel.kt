package com.ntphat.thesisk40.model

import android.util.Log
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.Http
import com.ntphat.thesisk40.presenter.LoginPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.UrlParser
import okhttp3.Callback
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class LoginModel(
        val loginPresenter: LoginPresenter
) : BaseModel() {

    fun login(code: String, pwd: String) {
        val request = apiRequestBuilder
                .url(UrlParser.getFullApiUrl(ApiUrl.LOGIN_PATH))
                .method(Http.POST)
                .body(mapOf(
                        "staff_code" to code,
                        "password" to pwd
                ))
                .build()

        val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                Log.e("LoginModel", e.toString())
                loginPresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                loginPresenter.onLoginResponse(JsonParser.fromJson(json))
            }
        })
    }
}