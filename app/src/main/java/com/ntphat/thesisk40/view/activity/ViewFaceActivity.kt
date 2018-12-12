package com.ntphat.thesisk40.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.adapter.StudentFaceAdapter
import com.ntphat.thesisk40.constant.IntentString
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.entity.StudentFace
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.presenter.ViewFacePresenter
import com.ntphat.thesisk40.presenter.impl.ViewFacePresenterImpl
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class ViewFaceActivity : AppCompatActivity(), ViewFacePresenter.View {

    val presenter = ViewFacePresenterImpl(this)
    lateinit var rvFace: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_face)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initRecyclerView()

        try {
            initData()
        } catch (e: InvalidExtraData) {
            presenter.handleError(e)
        }
    }

    private fun initData() {
        val code = intent.getStringExtra(IntentString.STUDENT_CODE) ?: ""
        val name = intent.getStringExtra(IntentString.STUDENT_NAME) ?: ""
        this.title = "Ảnh của: $name"
        presenter.fetchFaces(code, name)
    }

    private fun initRecyclerView() {
        rvFace = findViewById(R.id.rv_face)
        val lm = LinearLayoutManager(this)
        lm.orientation = LinearLayoutManager.VERTICAL
        rvFace.layoutManager = lm
    }

    override fun toggleLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAdapter(adapter: StudentFaceAdapter) {
        rvFace.adapter = adapter
    }

    override fun refreshList() {
        runOnUiThread {
            presenter.refreshList()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                exit()
                true
            }
            else -> {
                true
            }
        }
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

    override fun exit() {
        finish()
    }
}
