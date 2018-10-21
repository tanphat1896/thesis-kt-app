package com.ntphat.thesisk40.data

enum class LocalErrorInfo {
    NO_NETWORK {
        override fun code() = -1
        override fun message(additional: String) = "Không có kết nối mạng. $additional"
    },
    CONNECTION_FAILED {
        override fun code() = -2
        override fun message(additional: String) = "Lỗi kết nối tới server. $additional"
    },
    UNKNOWN {
        override fun code() = -999
        override fun message(additional: String) = "Lỗi không xác định. $additional"
    },
    INVALID_INTENT_EXTRA {
        override fun code() = -998
        override fun message(additional: String) = "Dữ liệu từ intent không hợp lệ. $additional"
    }
    ;
    abstract fun code(): Int
    abstract fun message(additional: String = ""): String
}