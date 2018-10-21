package com.ntphat.thesisk40.presenter

import android.content.Context
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.util.SharedPref
import java.lang.Exception
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

open class BasePresenter(private val v: Presenter.View) : Presenter {

    override fun getBearerToken(): String {
        val token = SharedPref.get(v as Context, App.ACCESS_TOKEN)
        return "Bearer $token"
    }

    override fun handleError(e: Exception) {
        if (e is SocketTimeoutException || e is NoRouteToHostException) {
            v.showError(Error(LocalErrorInfo.CONNECTION_FAILED))
            return
        }
        v.showError(Error(LocalErrorInfo.UNKNOWN))
    }
}