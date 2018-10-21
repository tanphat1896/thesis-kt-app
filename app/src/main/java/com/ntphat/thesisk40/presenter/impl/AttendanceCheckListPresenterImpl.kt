package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import android.view.View
import com.ntphat.thesisk40.adapter.StudentAdapter
import com.ntphat.thesisk40.data.response.StudentResponse
import com.ntphat.thesisk40.entity.Student
import com.ntphat.thesisk40.entity.TeachingClass
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.model.impl.StudentModelImpl
import com.ntphat.thesisk40.presenter.AttendanceCheckListPresenter
import com.ntphat.thesisk40.presenter.BasePresenter
import com.ntphat.thesisk40.util.JsonParser

class AttendanceCheckListPresenterImpl(
        val view: AttendanceCheckListPresenter.View
) : BasePresenter(view), AttendanceCheckListPresenter {

    private val studentModel = StudentModelImpl(this)

    private lateinit var teachingClass: TeachingClass
    private lateinit var studentAdapter: StudentAdapter

    private val vhStudents: MutableList<StudentAdapter.StudentViewHolder> = mutableListOf()
    private val presentedStudentIds: MutableList<Int> = mutableListOf()

    override fun initData(teachingClassJson: String) {
        teachingClass = JsonParser.fromJson(teachingClassJson)
                ?: throw InvalidExtraData("Không có dữ liệu lớp học phần")

        view.updateClassName("${teachingClass.name} - ${teachingClass.group}")

        studentAdapter = StudentAdapter(view as Context, vhStudents)
        view.setAdapter(studentAdapter)

        studentModel.fetchStudents(teachingClass.id)
    }

    override fun onStudentsFetched(studentResponse: StudentResponse) {
        if (studentResponse.isError()) {
            view.showError(studentResponse.error)
            view.finishActivity()
            return
        }

        vhStudents.clear()
        vhStudents.addAll(studentResponse.content.map {
            StudentAdapter.StudentViewHolder(it, false)
        })

        view.refreshList()
    }

    override fun toggleCheckPresented(view: View, position: Int) {
        vhStudents[position].present = !vhStudents[position].present
        refreshList()
    }

    override fun refreshList() {
        studentAdapter.notifyDataSetChanged()
    }
}