package com.ntphat.thesisk40.data

data class Error(
        val code: Int = 0,
        val message: String = "Lỗi không xác định."
) {
    constructor(errorInfo: LocalErrorInfo)
            : this(errorInfo.code(), errorInfo.message())

    override fun toString(): String {
        return "Lỗi $code: $message"
    }
}