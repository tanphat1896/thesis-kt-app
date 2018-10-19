package com.ntphat.thesisk40.util

import com.ntphat.thesisk40.constant.ApiUrl

class UrlParser {
    companion object {
        @JvmStatic
        fun getFullApiUrl(path: String): String {
            return if (Environment.isInProduction()) {
                "${ApiUrl.BASE_PRODUCTION}$path"
            } else {
                "${ApiUrl.BASE_DEVELOPMENT}$path"
            }
        }
    }
}