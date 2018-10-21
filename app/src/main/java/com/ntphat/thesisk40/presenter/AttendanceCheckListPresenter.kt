package com.ntphat.thesisk40.presenter

import android.view.View
import com.ntphat.thesisk40.adapter.StudentAdapter
import com.ntphat.thesisk40.data.response.StudentResponse

interface AttendanceCheckListPresenter : Presenter {

    fun initData(teachingClassJson: String)

    fun onStudentsFetched(studentResponse: StudentResponse)

    fun toggleCheckPresented(view: android.view.View, itemId: Int)

    fun refreshList()

    interface View : Presenter.View {
        fun updateClassName(className: String)

        fun toggleProgress(visible: Boolean)

        fun toggleMainContent(visible: Boolean)

        fun setAdapter(adapter: StudentAdapter)

        fun refreshList()

        fun finishActivity()
    }
}