package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import com.ntphat.thesisk40.adapter.FaceDetailAdapter
import com.ntphat.thesisk40.data.response.CommonResponse
import com.ntphat.thesisk40.entity.FaceCheckingResult
import com.ntphat.thesisk40.entity.FaceDetail
import com.ntphat.thesisk40.entity.TeachingClass
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.exception.PerformFailedException
import com.ntphat.thesisk40.model.impl.FaceCheckingModelImpl
import com.ntphat.thesisk40.presenter.BasePresenter
import com.ntphat.thesisk40.presenter.FaceCheckingPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.view.activity.ImageCheckingActivity
import java.io.File

class FaceCheckingPresenterImpl(
        val view: ImageCheckingActivity
) : BasePresenter(view), FaceCheckingPresenter {

    private val faceCheckingModel = FaceCheckingModelImpl(this)
    private lateinit var adapter: FaceDetailAdapter
    private lateinit var teachingClass: TeachingClass
    private lateinit var faceCheckingResult: FaceCheckingResult
    private val faceDetails: MutableList<FaceDetail> = mutableListOf()
    private var hasFaceData = false

    override fun initData(teachingClassJson: String) {
        teachingClass = JsonParser.fromJson(teachingClassJson)
                ?: throw InvalidExtraData("Không có dữ liệu lớp học phần")
        adapter = FaceDetailAdapter(
                view as Context,
                faceDetails
        )
        view.setAdapter(adapter)
    }

    override fun backToTakePicture() {
        view.confirmBack(hasFaceData)
    }

    override fun destroyData() {
        hasFaceData = false
        faceCheckingResult = FaceCheckingResult()
    }

    override fun check(imageStoragePath: String) {
        view.toggleProgress(true)
        val file = File(imageStoragePath)
        faceCheckingModel.check(teachingClass.id, file)
    }

    override fun onChecked(response: CommonResponse) {
        if (response.isError()) {
            view.showError(response.error)
            return
        }
        val result = JsonParser.fromJson<FaceCheckingResult>(response.content.toString())
                ?: handleError(PerformFailedException("Dữ liệu không hợp lệ"))

        hasFaceData = true
        faceCheckingResult = result as FaceCheckingResult

        faceDetails.clear()
        faceDetails.addAll(faceCheckingResult.imageDetail)

        view.showCheckResult(result.imageLink)
        view.refreshList()
    }

    override fun refreshList() {
        adapter.notifyDataSetChanged()
    }

    override fun backToCheckList() {
        view.backToCheckList(JsonParser.toJson(faceCheckingResult))
    }

    override fun viewFaces(position: Int) {
        val faceDetail = faceDetails[position]
        view.navigateToViewFace(faceDetail.student)
    }
}