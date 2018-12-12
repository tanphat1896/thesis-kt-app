package com.ntphat.thesisk40.model.impl

import android.util.Log
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.Http
import com.ntphat.thesisk40.data.response.CommonResponse
import com.ntphat.thesisk40.data.response.StudentFaceResponse
import com.ntphat.thesisk40.data.response.StudentResponse
import com.ntphat.thesisk40.model.BaseModel
import com.ntphat.thesisk40.model.FaceModel
import com.ntphat.thesisk40.presenter.ViewFacePresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.UrlParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class FaceModelImpl(
        val viewFacePresenter: ViewFacePresenter
) : BaseModel(), FaceModel {

    override fun fetchFaces(stdCode: String) {
        val request = apiRequestBuilder
                .url(UrlParser.getFullApiUrl("${ApiUrl.GET_STUDENT_FACES}/$stdCode/faces"))
                .header(Http.AUTH_HEADER, viewFacePresenter.getBearerToken())
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(javaClass.name, e.localizedMessage, e)
                viewFacePresenter.handleError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: ""
                viewFacePresenter.onFacesFetched(
                        JsonParser.fromJson(json) ?: StudentFaceResponse(listOf())
                )
            }
        })
    }
}