package com.ntphat.thesisk40.exception

import java.lang.RuntimeException

class InvalidExtraData(message: String = "Không có dữ liệu") : RuntimeException(message)