package com.ntphat.thesisk40.data

open class Response {
    private val status: String = "error"
    val error: Error = Error()

    fun isError(): Boolean {
        return status == "error"
    }
}