package com.ntphat.thesisk40.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.adapter.StudentAdapter
import com.ntphat.thesisk40.constant.IntentString
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.entity.Student
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.presenter.AttendanceCheckListPresenter
import com.ntphat.thesisk40.presenter.impl.AttendanceCheckListPresenterImpl
import com.ntphat.thesisk40.util.UiWidget
import com.ntphat.thesisk40.view.action.RecyclerTouchListener
import kotlinx.android.synthetic.main.student_item.view.*
import net.igenius.customcheckbox.CustomCheckBox

class AttendanceCheckListActivity : AppCompatActivity(), AttendanceCheckListPresenter.View {

    private val presenter = AttendanceCheckListPresenterImpl(this)

    private lateinit var progress: LinearLayout
    private lateinit var content: LinearLayout
    private lateinit var studentRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_check_list)

        progress = findViewById(R.id.progress)
        content = findViewById(R.id.main_content)

        initRecyclerView()

        try {
            presenter.initData(intent.getStringExtra(IntentString.TEACHING_CLASS) ?: "")
        } catch (e: InvalidExtraData) {
            showError(Error(
                    LocalErrorInfo.INVALID_INTENT_EXTRA.code(),
                    LocalErrorInfo.INVALID_INTENT_EXTRA.message(e.message ?: "")
            ))
            finish()
        }
    }

    private fun initRecyclerView() {
        studentRv = findViewById(R.id.list_student)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        studentRv.layoutManager = layoutManager

        studentRv.addOnItemTouchListener(RecyclerTouchListener(
                this,
                studentRv,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        presenter.toggleCheckPresented(view, position)
                    }
                    override fun onLongClick(view: View?, position: Int) {}
                }
        ))
    }

    override fun setAdapter(adapter: StudentAdapter) {
        studentRv.adapter = adapter
    }

    override fun updateClassName(className: String) {
        title = className
    }

    override fun showError(e: Error) {
        runOnUiThread {
            UiWidget.toast(this, e.toString(), true)
        }
    }

    override fun refreshList() {
        runOnUiThread {
            presenter.refreshList()
            toggleMainContent(true)
            toggleProgress(false)
        }
    }

    override fun finishActivity() {
        this.finish()
    }

    override fun toggleProgress(visible: Boolean) {
        progress.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun toggleMainContent(visible: Boolean) {
        content.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
