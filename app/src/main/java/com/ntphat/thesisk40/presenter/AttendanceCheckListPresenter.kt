package com.ntphat.thesisk40.presenter

import com.ntphat.thesisk40.adapter.StudentAdapter
import com.ntphat.thesisk40.data.response.CommonResponse
import com.ntphat.thesisk40.data.response.StudentResponse

interface AttendanceCheckListPresenter : Presenter {

    fun initData(teachingClassJson: String)

    fun fetchStudents()

    fun onStudentsFetched(studentResponse: StudentResponse)

    fun onSessionFetched(response: CommonResponse)

    fun onSaved(response: CommonResponse)

    fun toggleCheckPresented(view: android.view.View, itemId: Int)

    fun updateSession(session: Int)

    fun updateDate(date: String)

    fun refreshList()

    fun saveCheckList(confirm: Boolean)

    fun checkBeforeBack()

    fun startFaceCheck()

    fun checkPresentWithFaceResult(faceCheckResultJson: String)

    interface View : Presenter.View {
        fun initUi(className: String, date: String, session: Int)

        fun updatePresentAbsent(present: Int, absent: Int)

        fun toggleProgress(visible: Boolean)

        fun toggleMainContent(visible: Boolean)

        fun setAdapter(adapter: StudentAdapter)

        fun refreshList()

        fun confirmSave()

        fun showSaveResult(message: String)

        fun finishActivity()

        fun confirmBack()

        fun startFaceCheck(teachingClassJson: String)
    }
}