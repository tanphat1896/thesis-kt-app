package com.ntphat.thesisk40.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.adapter.TeachingClassAdapter
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.constant.IntentString
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.presenter.Initializer
import com.ntphat.thesisk40.presenter.SelectClassPresenter
import com.ntphat.thesisk40.presenter.impl.SelectClassPresenterImpl
import com.ntphat.thesisk40.util.UiWidget
import com.ntphat.thesisk40.view.action.RecyclerTouchListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class SelectClassActivity : AppCompatActivity(), SelectClassPresenter.View {

    private val presenter = SelectClassPresenterImpl(this)

    private lateinit var teachingClassRv: RecyclerView
    private lateinit var progress: LinearLayout
    private lateinit var content: LinearLayout
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_teaching_class)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progress = findViewById(R.id.progress)
        content = findViewById(R.id.main_content)

        initSearchView()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        presenter.initList()
    }

    private fun initSearchView() {
        searchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?) = false

            override fun onQueryTextChange(p0: String?): Boolean {
                presenter.onSearch(p0)
                return false
            }
        })
    }

    private fun initRecyclerView() {
        teachingClassRv = findViewById(R.id.list_teaching_class)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        teachingClassRv.layoutManager = layoutManager

        teachingClassRv.addOnItemTouchListener(RecyclerTouchListener(
                this,
                teachingClassRv,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        presenter.checkClass(position)
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                }
        ))
    }

    override fun setAdapter(adapter: TeachingClassAdapter) {
        teachingClassRv.adapter = adapter
    }

    override fun refreshList() {
        runOnUiThread {
            presenter.refreshList()
            toggleMainContent(true)
            toggleProgress(false)
        }
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

    override fun navigateToCheckListActivity(teachingClassJson: String) {
        val i = Intent(this, AttendanceCheckListActivity::class.java)
        i.putExtra(IntentString.TEACHING_CLASS, teachingClassJson)
        startActivity(i)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
