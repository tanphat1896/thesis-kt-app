package com.ntphat.thesisk40.model.impl

import android.util.Log
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.Http
import com.ntphat.thesisk40.data.response.CommonResponse
import com.ntphat.thesisk40.model.BaseModel
import com.ntphat.thesisk40.model.FaceCheckingModel
import com.ntphat.thesisk40.presenter.FaceCheckingPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.UrlParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException

class FaceCheckingModelImpl(
        private val faceCheckingPresenter: FaceCheckingPresenter
): BaseModel(), FaceCheckingModel {

    override fun check(teachingClassId: Int, file: File) {
        val request = apiRequestBuilder.method("post")
                .url(UrlParser.getFullApiUrl(ApiUrl.POST_FACE_CHECKING))
                .header(Http.AUTH_HEADER, faceCheckingPresenter.getBearerToken())
                .file(file, mapOf("id" to teachingClassId))
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(javaClass.name, e.localizedMessage, e)
                faceCheckingPresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                Log.i(javaClass.name, json)
                faceCheckingPresenter.onChecked(
                        JsonParser.fromJson(json) ?: CommonResponse()
                )
            }
        })
    }
}