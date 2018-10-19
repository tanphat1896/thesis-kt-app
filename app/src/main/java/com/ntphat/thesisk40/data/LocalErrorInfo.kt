package com.ntphat.thesisk40.data

enum class LocalErrorInfo {
    NO_NETWORK {
        override fun code() = -1
        override fun message() = "Không có kết nối mạng."
    },
    CONNECTION_FAILED {
        override fun code() = -2
        override fun message() = "Lỗi kết nối tới server."
    },
    UNKNOWN {
        override fun code() = -999
        override fun message() = "Lỗi không xác định."
    }
    ;
    abstract fun code(): Int
    abstract fun message(): String
}