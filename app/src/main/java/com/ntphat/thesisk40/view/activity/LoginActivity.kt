package com.ntphat.thesisk40.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.presenter.impl.LoginPresenterImpl
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.presenter.Initializer
import com.ntphat.thesisk40.presenter.LoginPresenter
import com.ntphat.thesisk40.util.Networking
import com.ntphat.thesisk40.util.SharedPref
import com.ntphat.thesisk40.util.UiWidget

class LoginActivity : AppCompatActivity(), LoginPresenter.View, Initializer {

    private val loginPresenter = LoginPresenterImpl(this)

    private lateinit var errorText: TextView
    private lateinit var textCode: TextInputEditText
    private lateinit var textPwd: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: LinearLayout
    private lateinit var loginForm: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        errorText = findViewById(R.id.login_error)
        textCode = findViewById(R.id.code)
        textPwd = findViewById(R.id.password)
        btnLogin = findViewById(R.id.login_btn)
        progressBar = findViewById(R.id.progress)
        loginForm = findViewById(R.id.login_form)

        btnLogin.setOnClickListener {
            login()
        }

        loginPresenter.loadStaffCode()
    }

    override fun loadProperties() {
        this.assets.open("application.properties")?.apply {
            App.PROPERTIES.load(this)
        }
    }

    override fun fillStaffCode(code: String) {
        if (code.isEmpty()) {
            return
        }
        (textCode as EditText).setText(code)
        textPwd.requestFocus()
        UiWidget.toggleSoftKeyboard(this, false, textPwd)
    }

    override fun toggleProgress(visible: Boolean) {
        progressBar.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun toggleLoginForm(visible: Boolean) {
        loginForm.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun navigateToHome() {
        runOnUiThread {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun showLoginError(error: Error) {
        runOnUiThread {
            toggleProgress(false)
            toggleLoginForm(true)
            showError(error)
        }
    }

    private fun login() {
        UiWidget.toggleSoftKeyboard(this)
        clearError()

        if (!Networking.isActivated(this)) {
            showError(Error(LocalErrorInfo.NO_NETWORK))
            return
        }

        val code = textCode.text.toString().trim()
        val pwd = textPwd.text.toString()
        val valid = validate(code, pwd)

        if (!valid) {
            return
        }

        toggleProgress(true)
        toggleLoginForm(false)

        loginPresenter.login(code, pwd)
    }

    private fun clearError() {
        errorText.text = ""
    }

    private fun validate(code: String, pwd: String): Boolean {
        val regex = """[A-Za-z0-9_]+""".toRegex()
        if (code.isBlank()) {
            textCode.error = "Hãy nhập mã cán bộ"
            textCode.requestFocus()
            return false
        }
        if (pwd.isEmpty()) {
            textPwd.error = "Hãy nhập mật khẩu"
            textPwd.requestFocus()
            return false
        }
        if (!code.isBlank() && !regex.matches(code)) {
            textCode.error = "Mã cán bộ không hợp lệ"
            textCode.requestFocus()
            return false
        }
        return true
    }

    override fun showError(e: Error) {
        errorText.text = e.toString()
    }
}
