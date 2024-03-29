package com.ntphat.thesisk40.presenter

import com.ntphat.thesisk40.adapter.FaceDetailAdapter
import com.ntphat.thesisk40.data.response.CommonResponse
import com.ntphat.thesisk40.entity.Student

interface FaceCheckingPresenter : Presenter {

    fun initData(teachingClassJson: String)

    fun backToTakePicture()

    fun destroyData()

    fun check(imageStoragePath: String)

    fun onChecked(response: CommonResponse)

    fun refreshList()

    fun backToCheckList()

    fun viewFaces(position: Int)

    interface View : Presenter.View {
        fun toggleTakePicture(visible: Boolean)

        fun toggleProgress(visible: Boolean)

        fun toggleCheckResult(visible: Boolean)

        fun showCheckResult(imageLink: String)

        fun confirmBack(hasFaceData: Boolean)

        fun setAdapter(adapter: FaceDetailAdapter)

        fun refreshList()

        fun backToCheckList(faceCheckResultJson: String)

        fun navigateToViewFace(student: Student)
    }
}