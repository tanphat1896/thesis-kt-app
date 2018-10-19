package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import android.util.Log
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.data.response.LoginResponse
import com.ntphat.thesisk40.model.LoginModel
import com.ntphat.thesisk40.presenter.LoginPresenter
import com.ntphat.thesisk40.util.SharedPref
import java.lang.Exception
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

class LoginPresenterImpl(
        private val loginActivity: LoginPresenter.View
) : LoginPresenter {

    private val loginModel = LoginModel(this)

    override fun login(code: String, password: String) {
        storeStaffCode(code)
        loginModel.login(code, password);
    }

    override fun onLoginResponse(loginResponse: LoginResponse?) {
        val response = loginResponse ?: LoginResponse()
        if (response.isError()) {
            loginActivity.showLoginError(response.error)
            clearStaffCode()
            return
        }

        storeToken(response.content)
        loginActivity.navigateToHome()
    }

    override fun storeToken(token: String) {
        SharedPref.store(loginActivity as Context, App.ACCESS_TOKEN, token)
    }

    override fun storeStaffCode(code: String) {
        SharedPref.store(loginActivity as Context, App.STAFF_CODE_TOKEN, code)
    }

    override fun clearStaffCode() {
        SharedPref.store(loginActivity as Context, App.STAFF_CODE_TOKEN, "")
    }

    override fun loadStaffCode() {
        val staffCode = SharedPref.get(loginActivity as Context, App.STAFF_CODE_TOKEN)
        Log.e(javaClass.name, staffCode)
        loginActivity.fillStaffCode(staffCode)
    }

    override fun handleError(e: Exception) {
        if (e is SocketTimeoutException || e is NoRouteToHostException) {
            loginActivity.showLoginError(Error(LocalErrorInfo.CONNECTION_FAILED))
            return
        }
        loginActivity.showLoginError(Error(LocalErrorInfo.UNKNOWN))
    }
}