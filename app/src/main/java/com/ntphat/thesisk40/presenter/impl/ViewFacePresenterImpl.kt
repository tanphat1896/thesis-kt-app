package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import com.ntphat.thesisk40.adapter.StudentFaceAdapter
import com.ntphat.thesisk40.data.response.StudentFaceResponse
import com.ntphat.thesisk40.entity.StudentFace
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.model.impl.FaceModelImpl
import com.ntphat.thesisk40.presenter.BasePresenter
import com.ntphat.thesisk40.presenter.ViewFacePresenter

class ViewFacePresenterImpl(
        val view: ViewFacePresenter.View
) : BasePresenter(view), ViewFacePresenter {

    private val faceModel = FaceModelImpl(this)
    private val studentFaces = mutableListOf<StudentFace>()
    private lateinit var adapter: StudentFaceAdapter

    override fun fetchFaces(studentCode: String, studentName: String) {
        if (studentCode.isEmpty()) {
            throw InvalidExtraData("Mã sinh viên không hợp lệ")
        }

        initData()

        faceModel.fetchFaces(studentCode)
    }

    private fun initData() {
        adapter = StudentFaceAdapter(
                view as Context,
                studentFaces
        )
        view.setAdapter(adapter)
    }

    override fun onFacesFetched(response: StudentFaceResponse) {
        if (response.isError()) {
            view.showError(response.error)
            return
        }

        studentFaces.clear()
        studentFaces.addAll(response.content)

        view.refreshList()
    }

    override fun refreshList() {
        adapter.notifyDataSetChanged()
    }
}