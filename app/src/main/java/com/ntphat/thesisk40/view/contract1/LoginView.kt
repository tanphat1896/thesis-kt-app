package com.ntphat.thesisk40.view.contract1

import com.ntphat.thesisk40.data.Error

interface LoginView : BaseView {
    fun storeToken(token: String)
    fun goHome()
    fun showLoginError(error: Error)
}