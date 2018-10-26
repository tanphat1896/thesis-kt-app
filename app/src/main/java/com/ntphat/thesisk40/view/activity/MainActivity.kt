package com.ntphat.thesisk40.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.util.UiWidget
import com.ntphat.thesisk40.view.contract1.MainView


class MainActivity : AppCompatActivity(), MainView {
    private lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavBar = findViewById(R.id.bottomNavBar)

        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.schedule -> UiWidget.snackBar(this, "Schedule")
                R.id.check -> {
                    UiWidget.snackBar(this, "Checking")
                    startActivity(Intent(this, SelectClassActivity::class.java))
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Thoát ứng dụng")
                .setMessage("Xác nhận thoát ứng dụng?")
                .setPositiveButton("Đồng ý") { _, _ -> finish() }
                .setNegativeButton("Hủy bỏ", null)
                .show()
    }

    override fun showError(error: Error) {

    }
}
