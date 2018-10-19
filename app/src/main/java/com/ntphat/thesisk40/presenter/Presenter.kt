package com.ntphat.thesisk40.presenter

import com.ntphat.thesisk40.data.Error
import java.lang.Exception

interface Presenter {
    fun handleError(e: Exception)

    interface View {
        fun showError(e: Error)
    }
}