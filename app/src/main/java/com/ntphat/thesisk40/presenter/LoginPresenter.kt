package com.ntphat.thesisk40.presenter

import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.data.response.LoginResponse

interface LoginPresenter : Presenter {

    /**
     * Gọi Api đăng nhập hệ thống
     */
    fun login(code: String, password: String)

    /**
     * Action khi api trả kết quả về
     */
    fun onLoginResponse(loginResponse: LoginResponse?)

    /**
     * Lưu access token
     */
    fun storeToken(token: String)

    /**
     * Lưu mã cán bộ để đăng nhập lần sau
     */
    fun storeStaffCode(code: String)

    fun clearStaffCode()

    fun loadStaffCode()

    interface View : Presenter.View {
        fun fillStaffCode(code: String)

        fun toggleProgress(visible: Boolean)

        fun toggleLoginForm(visible: Boolean)

        fun showLoginError(error: Error)

        fun navigateToHome()
    }
}