package com.ntphat.thesisk40.model.impl

import android.util.Log
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.Http
import com.ntphat.thesisk40.data.response.ScheduleResponse
import com.ntphat.thesisk40.model.BaseModel
import com.ntphat.thesisk40.model.ScheduleModel
import com.ntphat.thesisk40.presenter.MainPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.UrlParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class ScheduleModelImpl(
        val mainPresenter: MainPresenter
) : BaseModel(), ScheduleModel {

    override fun fetchSchedule() {
        val request = apiRequestBuilder
                .header(Http.AUTH_HEADER, mainPresenter.getBearerToken())
                .url(UrlParser.getFullApiUrl("${ApiUrl.GET_SCHEDULE}/today"))
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ScheduleModelImpl", e.localizedMessage, e)
                mainPresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                mainPresenter.onScheduleFetched(
                        JsonParser.fromJson(json) ?: ScheduleResponse(listOf())
                )
            }
        })
    }
}