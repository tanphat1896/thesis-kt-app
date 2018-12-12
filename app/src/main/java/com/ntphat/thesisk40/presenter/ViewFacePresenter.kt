package com.ntphat.thesisk40.presenter

import com.ntphat.thesisk40.adapter.StudentFaceAdapter
import com.ntphat.thesisk40.data.response.StudentFaceResponse

interface ViewFacePresenter : Presenter {

    fun fetchFaces(studentCode: String, studentName: String)

    fun onFacesFetched(response: StudentFaceResponse)

    fun refreshList()

    interface View : Presenter.View {

        fun toggleLoading()

        fun setAdapter(adapter: StudentFaceAdapter)

        fun refreshList()
    }
}