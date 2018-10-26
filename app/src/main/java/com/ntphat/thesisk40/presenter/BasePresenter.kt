package com.ntphat.thesisk40.presenter

import android.content.Context
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.LocalErrorInfo
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.exception.PerformFailedException
import com.ntphat.thesisk40.util.SharedPref
import java.io.FileNotFoundException
import java.lang.Exception
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

open class BasePresenter(private val v: Presenter.View) : Presenter {
    var isBusinessError = true

    override fun getBearerToken(): String {
        val token = SharedPref.get(v as Context, App.ACCESS_TOKEN)
        return "Bearer $token"
    }

    override fun handleError(e: Exception) {
        if (e is ConnectException) {
            v.showError(Error(LocalErrorInfo.NO_NETWORK))
            isBusinessError = false
            return
        }
        if (e is SocketTimeoutException || e is NoRouteToHostException) {
            v.showError(Error(LocalErrorInfo.CONNECTION_FAILED))
            isBusinessError = false
            return
        }
        if (e is PerformFailedException) {
            v.showError(Error(
                    LocalErrorInfo.INVALID_INTENT_EXTRA.code(),
                    LocalErrorInfo.INVALID_INTENT_EXTRA.message(e.message ?: "")
            ))
            isBusinessError = false
            return
        }
        if (e is InvalidExtraData) {
            v.showError(Error(
                    LocalErrorInfo.INVALID_INTENT_EXTRA.code(),
                    LocalErrorInfo.INVALID_INTENT_EXTRA.message(e.message ?: "")
            ))
            isBusinessError = false
            return
        }
        if (e is FileNotFoundException) {
            v.showError(Error(
                    LocalErrorInfo.FILE_NOT_FOUND.code(),
                    LocalErrorInfo.FILE_NOT_FOUND.message(e.localizedMessage)
            ))
            return
        }
        v.showError(Error(LocalErrorInfo.UNKNOWN))
    }
}