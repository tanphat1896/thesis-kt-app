package com.ntphat.thesisk40.util

import com.ntphat.thesisk40.constant.App

class Environment {
    companion object {
        fun isInProduction(): Boolean {
            return App.PROPERTIES["app.env"] == "production"
        }
    }
}