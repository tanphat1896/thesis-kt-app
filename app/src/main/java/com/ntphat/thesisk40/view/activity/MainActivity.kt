package com.ntphat.thesisk40.view.activity

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.util.SharedPref
import com.ntphat.thesisk40.view.contract1.MainView


class MainActivity : AppCompatActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.token).text = SharedPref.get(this, App.ACCESS_TOKEN)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Thoát ứng dụng")
                .setMessage("Xác nhận thoát ứng dụng?")
                .setPositiveButton("Đồng ý", { _, _ -> finish() })
                .setNegativeButton("Hủy bỏ", null)
                .show()
    }

    override fun showError(error: Error) {

    }
}
