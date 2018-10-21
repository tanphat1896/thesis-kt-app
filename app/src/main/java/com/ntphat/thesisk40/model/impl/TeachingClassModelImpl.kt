package com.ntphat.thesisk40.model.impl

import android.util.Log
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.Http
import com.ntphat.thesisk40.data.response.TeachingClassResponse
import com.ntphat.thesisk40.model.BaseModel
import com.ntphat.thesisk40.model.TeachingClassModel
import com.ntphat.thesisk40.presenter.SelectClassPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.UrlParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class TeachingClassModelImpl(
        val selectClassPresenter: SelectClassPresenter
) : BaseModel(), TeachingClassModel {

    override fun fetchTeachingClass() {
        val request = apiRequestBuilder
                .header(Http.AUTH_HEADER, selectClassPresenter.getBearerToken())
                .url(UrlParser.getFullApiUrl(ApiUrl.GET_TEACHING_CLASS))
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TeachingClassModelImpl", e.localizedMessage, e)
                selectClassPresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                selectClassPresenter.onTeachingClassFetched(
                        JsonParser.fromJson(json) ?: TeachingClassResponse(listOf())
                )
            }
        })
    }
}