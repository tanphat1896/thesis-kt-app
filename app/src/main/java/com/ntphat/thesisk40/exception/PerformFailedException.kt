package com.ntphat.thesisk40.exception

import java.lang.Exception
import java.lang.RuntimeException

class PerformFailedException(message: String = "Thao tác thất bại") : RuntimeException(message)
