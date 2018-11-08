package com.ntphat.thesisk40.view.activity

import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.adapter.ScheduleAdapter
import com.ntphat.thesisk40.constant.ApiUrl
import com.ntphat.thesisk40.constant.IntentString
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.presenter.MainPresenter
import com.ntphat.thesisk40.presenter.impl.MainPresenterImpl
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*


class MainActivity : AppCompatActivity(), MainPresenter.View {
    private lateinit var bottomNavBar: BottomNavigationView

    private val presenter = MainPresenterImpl(this)

    private lateinit var progress: LinearLayout
    private lateinit var content: LinearLayout
    private lateinit var scheduleRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress)
        content = findViewById(R.id.main_content)

        initRecyclerView()

        bottomNavBar = findViewById(R.id.bottomNavBar)

        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.schedule -> UiWidget.snackBar(this, "Schedule")
                R.id.check -> {
//                    UiWidget.snackBar(this, "Checking")
                    startActivity(Intent(this, SelectClassActivity::class.java))
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun initRecyclerView() {
        scheduleRv = findViewById(R.id.list_schedule)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        scheduleRv.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
        bottomNavBar.selectedItemId = R.id.schedule
        presenter.initList()
    }

    override fun setAdapter(adapter: ScheduleAdapter) {
        scheduleRv.adapter = adapter
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.open_web -> {
                val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("${ApiUrl.BASE_PRODUCTION}${IntentString.SCHEDULE_WEB_URL}")
                )
                startActivity(intent)
                true
            }
            R.id.open_cal -> {
                val builder = CalendarContract.CONTENT_URI.buildUpon()
                builder.appendPath("time")
                ContentUris.appendId(builder, Calendar.getInstance().timeInMillis)
                val intent = Intent(Intent.ACTION_VIEW, builder.build())
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setIcon(R.drawable.help)
                .setTitle("Thoát ứng dụng")
                .setMessage("Xác nhận thoát ứng dụng?")
                .setPositiveButton("Đồng ý") { _, _ -> exit() }
                .setNegativeButton("Hủy bỏ", null)
                .show()
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
        this.finish()
    }
}
