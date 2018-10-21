package com.ntphat.thesisk40.model.impl

import android.util.Log
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.Http
import com.ntphat.thesisk40.data.response.StudentResponse
import com.ntphat.thesisk40.model.BaseModel
import com.ntphat.thesisk40.model.StudentModel
import com.ntphat.thesisk40.presenter.AttendanceCheckListPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.UrlParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class StudentModelImpl(
        val attendanceCheckListPresenter: AttendanceCheckListPresenter
) : BaseModel(), StudentModel {

    override fun fetchStudents(teachingClassId: Int) {
        val request = apiRequestBuilder
                .url(UrlParser.getFullApiUrl("${ApiUrl.GET_TEACHING_CLASS}/$teachingClassId/students"))
                .header(Http.AUTH_HEADER, attendanceCheckListPresenter.getBearerToken())
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(javaClass.name, e.localizedMessage, e)
                attendanceCheckListPresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                attendanceCheckListPresenter.onStudentsFetched(
                        JsonParser.fromJson(json) ?: StudentResponse(listOf())
                )
            }
        })
    }
}