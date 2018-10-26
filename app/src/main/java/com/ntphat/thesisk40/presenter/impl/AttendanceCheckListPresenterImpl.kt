package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import android.view.View
import com.ntphat.thesisk40.adapter.StudentAdapter
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.data.response.CommonResponse
import com.ntphat.thesisk40.data.response.StudentResponse
import com.ntphat.thesisk40.entity.FaceCheckingResult
import com.ntphat.thesisk40.entity.TeachingClass
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.model.impl.AttendanceModelImpl
import com.ntphat.thesisk40.model.impl.StudentModelImpl
import com.ntphat.thesisk40.presenter.AttendanceCheckListPresenter
import com.ntphat.thesisk40.presenter.BasePresenter
import com.ntphat.thesisk40.util.Environment
import com.ntphat.thesisk40.util.JsonParser

class AttendanceCheckListPresenterImpl(
        val view: AttendanceCheckListPresenter.View
) : BasePresenter(view), AttendanceCheckListPresenter {

    private val studentModel = StudentModelImpl(this)
    private val attendanceModel = AttendanceModelImpl(this)

    private lateinit var teachingClass: TeachingClass
    private lateinit var studentAdapter: StudentAdapter

    private val vhStudents: MutableList<StudentAdapter.StudentViewHolder> = mutableListOf()
    private val studentImages: MutableMap<String, String> = mutableMapOf()
    private var totalPresent: Int = 0
    private lateinit var date: String
    private var session: Int = 1
    private var saved = false

    override fun initData(teachingClassJson: String) {
        teachingClass = JsonParser.fromJson(teachingClassJson)
                ?: throw InvalidExtraData("Không có dữ liệu lớp học phần")

        studentAdapter = StudentAdapter(view as Context, vhStudents)
        view.setAdapter(studentAdapter)

        attendanceModel.fetchNextSession(teachingClass.id)
    }

    override fun fetchStudents() {
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

    override fun onSessionFetched(response: CommonResponse) {
        if (response.isError()) {
            view.showError(response.error)
            view.finishActivity()
            return
        }

        val session = response.content?.toString()?.toFloat()?.toInt()

        if (session == null) {
            view.showError(Error(
                    LocalErrorInfo.CUSTOM.code(),
                    LocalErrorInfo.CUSTOM.message("Buổi điểm danh không hợp lệ")
            ))
            view.finishActivity()
            return
        }

        this.session = session
        this.date = Environment.formatDate(Environment.todayDmy())

        view.initUi(
                "${teachingClass.name} - ${teachingClass.group}",
                Environment.todayDmy(),
                session
        )
    }

    override fun onSaved(response: CommonResponse) {
        if (response.isError()) {
            view.showError(response.error)
            return
        }
        saved = true
        view.showSaveResult(response.content?.toString() ?: "")
    }

    override fun toggleCheckPresented(view: View, itemId: Int) {
        if (saved) {
            return
        }
        val presented = vhStudents[itemId].present
        vhStudents[itemId].present = !vhStudents[itemId].present
        totalPresent += if (presented) -1 else 1

        this.view.updatePresentAbsent(totalPresent, vhStudents.size - totalPresent)
        refreshList()
    }

    override fun updateSession(session: Int) {
        this.session = session
    }

    override fun updateDate(date: String) {
        this.date = Environment.formatDate(date)
    }

    override fun refreshList() {
        studentAdapter.notifyDataSetChanged()
    }

    override fun saveCheckList(confirm: Boolean) {
        if (!confirm) {
            return view.confirmSave()
        }

        if (vhStudents.none { it.present }) {
            view.showError(Error(
                    LocalErrorInfo.CUSTOM.code(),
                    LocalErrorInfo.CUSTOM.message("Không có sinh viên nào có mặt!")
            ))
            return
        }

        attendanceModel.save(teachingClass.id, date, session, getPresentStudentIds(), studentImages)
    }

    private fun getPresentStudentIds(): List<Int> {
        return vhStudents.asSequence()
                .filter { it.present }
                .map { it.student.id }
                .toList()
    }

    override fun checkBeforeBack() {
        if (vhStudents.none { it.present } || saved) {
            return view.finishActivity()
        }
        view.confirmBack()
    }

    override fun startFaceCheck() {
        val json = JsonParser.toJson(teachingClass)
        view.startFaceCheck(json)
    }

    override fun checkPresentWithFaceResult(faceCheckResultJson: String) {
        val faceCheckingResult = JsonParser.fromJson<FaceCheckingResult>(faceCheckResultJson)
            ?: return view.showError(Error(LocalErrorInfo.INVALID_INTENT_EXTRA))
        faceCheckingResult.imageDetail.forEach {
            vhStudents.find { vhStd -> vhStd.student.id == it.student.id }?.apply {
                this.present = true
                studentImages["image-std-${it.student.id}"] = faceCheckingResult.imageLink
                totalPresent++
            }
        }
        this.view.updatePresentAbsent(totalPresent, vhStudents.size - totalPresent)
        refreshList()
    }
}