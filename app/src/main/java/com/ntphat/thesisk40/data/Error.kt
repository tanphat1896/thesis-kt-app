package com.ntphat.thesisk40.data

data class Error(
        val code: Int = LocalErrorInfo.UNKNOWN.code(),
        val message: String = LocalErrorInfo.UNKNOWN.message()
) {
    constructor(errorInfo: LocalErrorInfo)
            : this(errorInfo.code(), errorInfo.message())

    override fun toString(): String {
        return "Lỗi $code: $message"
    }
}