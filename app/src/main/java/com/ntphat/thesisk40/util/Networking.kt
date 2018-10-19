package com.ntphat.thesisk40.util

import android.content.Context
import android.net.ConnectivityManager

class Networking {
    companion object {
        @JvmStatic
        fun isActivated(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) ?: return false

            val networkInfo = (manager as ConnectivityManager).activeNetworkInfo

            return networkInfo != null && networkInfo.isConnected
        }
    }
}