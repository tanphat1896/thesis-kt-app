package com.ntphat.thesisk40.view.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.adapter.StudentAdapter
import com.ntphat.thesisk40.constant.IntentString
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.presenter.AttendanceCheckListPresenter
import com.ntphat.thesisk40.presenter.impl.AttendanceCheckListPresenterImpl
import com.ntphat.thesisk40.util.UiWidget
import com.ntphat.thesisk40.view.action.RecyclerTouchListener
import java.text.SimpleDateFormat
import java.util.*
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.NumberPicker
import org.jetbrains.anko.*


class AttendanceCheckListActivity : AppCompatActivity(), AttendanceCheckListPresenter.View {

    private val presenter = AttendanceCheckListPresenterImpl(this)

    private lateinit var progress: LinearLayout
    private lateinit var content: LinearLayout
    private lateinit var studentRv: RecyclerView

    private lateinit var textClassName: TextView
    private lateinit var textDate: TextView
    private lateinit var textSession: TextView
    private lateinit var textPresent: TextView
    private lateinit var textAbsent: TextView

    companion object {
        @JvmStatic
        val REQUEST_ID_IMAGE_CHECKING = 9999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_check_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progress = findViewById(R.id.progress)
        content = findViewById(R.id.main_content)

        textClassName = findViewById(R.id.text_class_name)
        textDate = findViewById(R.id.text_date_value)
        textSession = findViewById(R.id.text_session_value)
        textPresent = findViewById(R.id.text_present_value)
        textAbsent = findViewById(R.id.text_absent_value)

        initRecyclerView()
        initChangeButton()

        try {
            presenter.initData(intent.getStringExtra(IntentString.TEACHING_CLASS) ?: "")
        } catch (e: InvalidExtraData) {
            presenter.handleError(e)
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

    @SuppressLint("SimpleDateFormat")
    private fun initChangeButton() {
        val date = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, y)
            cal.set(Calendar.MONTH, m)
            cal.set(Calendar.DAY_OF_MONTH, d)
            val format = "dd-MM-yyyy"
            val df = SimpleDateFormat(format)
            textDate.text = df.format(cal.time)
            presenter.updateDate(df.format(cal.time))
        }

        findViewById<ImageButton>(R.id.btn_change_date).setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        findViewById<ImageButton>(R.id.btn_change_session).setOnClickListener {
            val sessions = Array(20) { i -> (i + 1).toString() }
            selector("Chọn buổi điểm danh", sessions.toList()) { dialogInterface, i ->
                textSession.text = sessions[i]
                presenter.updateSession(sessions[i].toInt())
            }
        }
    }

    override fun setAdapter(adapter: StudentAdapter) {
        studentRv.adapter = adapter
    }

    override fun initUi(className: String, date: String, session: Int) {
        runOnUiThread {
            textClassName.text = className
            textDate.text = date
            textSession.text = session.toString()
            presenter.fetchStudents()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updatePresentAbsent(present: Int, absent: Int) {
        textPresent.text = present.toString()
        textAbsent.text = " / $absent"
    }

    override fun showError(e: Error) {
        runOnUiThread {
            alert(e.toString()) {
                yesButton {
                    if (!presenter.isBusinessError) {
                        exit()
                    }
                }
            }.show()
        }
    }

    override fun showSaveResult(message: String) {
        runOnUiThread {
            alert(message) {
                yesButton { }
            }.show()
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

    override fun confirmSave() {
        alert("Sau khi lưu sẽ không thể thay đổi?", "Xác nhận lưu") {
            yesButton { presenter.saveCheckList(true) }
            noButton {}
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_attendance_checklist_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.image_checking -> {
                presenter.startFaceCheck()
                true
            }
            R.id.save_checklist -> {
                presenter.saveCheckList(false)
                true
            }
            android.R.id.home -> {
                presenter.checkBeforeBack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun confirmBack() {
        alert("Dữ liệu bạn chưa lưu sẽ bị mất?", "Xác nhận quay về") {
            yesButton { finish() }
            noButton {}
        }.show()
    }

    override fun exit() {
        finish()
    }

    override fun startFaceCheck(teachingClassJson: String) {
        val i = Intent(this, ImageCheckingActivity::class.java)
        i.putExtra(IntentString.TEACHING_CLASS, teachingClassJson)
        startActivityForResult(i, REQUEST_ID_IMAGE_CHECKING)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            presenter.checkPresentWithFaceResult(it.getStringExtra(IntentString.FACE_CHECK_RESULT))
        }
    }
}
