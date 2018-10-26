package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import android.util.Log
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.data.response.LoginResponse
import com.ntphat.thesisk40.model.LoginModel
import com.ntphat.thesisk40.presenter.BasePresenter
import com.ntphat.thesisk40.presenter.LoginPresenter
import com.ntphat.thesisk40.util.SharedPref
import java.lang.Exception
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

class LoginPresenterImpl(
        private val view: LoginPresenter.View
) : BasePresenter(view), LoginPresenter {

    private val loginModel = LoginModel(this)

    override fun login(code: String, password: String) {
        storeStaffCode(code)
        loginModel.login(code, password);
    }

    override fun onLoginResponse(loginResponse: LoginResponse?) {
        val response = loginResponse ?: LoginResponse()
        if (response.isError()) {
            view.showLoginError(response.error)
            clearStaffCode()
            return
        }

        storeToken(response.content)
        view.navigateToHome()
    }

    override fun storeToken(token: String) {
        SharedPref.store(view as Context, App.ACCESS_TOKEN, token)
    }

    override fun storeStaffCode(code: String) {
        SharedPref.store(view as Context, App.STAFF_CODE_TOKEN, code)
    }

    override fun clearStaffCode() {
        SharedPref.store(view as Context, App.STAFF_CODE_TOKEN, "")
    }

    override fun loadStaffCode() {
        val staffCode = SharedPref.get(view as Context, App.STAFF_CODE_TOKEN)
        Log.e(javaClass.name, staffCode)
        view.fillStaffCode(staffCode)
    }

    override fun handleError(e: Exception) {
        Log.e(javaClass.name, e.localizedMessage, e)
        if (e is SocketTimeoutException || e is NoRouteToHostException) {
            view.showLoginError(Error(LocalErrorInfo.CONNECTION_FAILED))
            return
        }
        view.showLoginError(Error(LocalErrorInfo.UNKNOWN))
    }
}