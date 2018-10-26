package com.ntphat.thesisk40.model.impl

import android.util.Log
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.Http
import com.ntphat.thesisk40.data.response.CommonResponse
import com.ntphat.thesisk40.model.AttendanceModel
import com.ntphat.thesisk40.model.BaseModel
import com.ntphat.thesisk40.presenter.AttendanceCheckListPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.UrlParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class AttendanceModelImpl(
        val attendanceCheckListPresenter: AttendanceCheckListPresenter
) : BaseModel(), AttendanceModel {

    override fun fetchNextSession(teachingClassId: Int) {
        val request = apiRequestBuilder
                .url(UrlParser.getFullApiUrl("${ApiUrl.GET_TEACHING_CLASS}/$teachingClassId/histories/next_session"))
                .header(Http.AUTH_HEADER, attendanceCheckListPresenter.getBearerToken())
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(javaClass.name, e.localizedMessage, e)
                attendanceCheckListPresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                attendanceCheckListPresenter.onSessionFetched(
                        JsonParser.fromJson(json) ?: CommonResponse(null)
                )
            }
        })
    }

    override fun save(teachingClassId: Int, date: String, session: Int, studentIds: List<Int>, studentImgs: Map<String, String>) {
        val body = mutableMapOf(
                "date" to date,
                "session" to session
        )
        var count = 0
        studentIds.forEach { body["ids[${count++}]"] = it }
        studentImgs.forEach { (key, value) -> body[key] = value }

        val request = apiRequestBuilder
                .url(UrlParser.getFullApiUrl("${ApiUrl.GET_TEACHING_CLASS}/$teachingClassId/histories"))
                .method(Http.POST)
                .header(Http.AUTH_HEADER, attendanceCheckListPresenter.getBearerToken())
                .body(body.toMap())
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(javaClass.name, e.localizedMessage, e)
                attendanceCheckListPresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                attendanceCheckListPresenter.onSaved(
                        JsonParser.fromJson(json) ?: CommonResponse(null)
                )
            }
        })
    }

}