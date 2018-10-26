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
    INVALID_INTENT_EXTRA {
        override fun code() = -3
        override fun message(additional: String) = "Dữ liệu từ intent không hợp lệ. $additional"
    },
    FILE_NOT_FOUND {
        override fun code() = -4
        override fun message(additional: String) = "File không tồn tại. $additional"
    },
    OPERATION_FAILED {
        override fun code() = -5
        override fun message(additional: String) = "Thao tác thất bại. $additional"
    },
    UNKNOWN {
        override fun code() = -999
        override fun message(additional: String) = "Lỗi không xác định. $additional"
    },
    CUSTOM {
        override fun code() = -997
        override fun message(additional: String) = additional
    }
    ;
    abstract fun code(): Int
    abstract fun message(additional: String = ""): String
}