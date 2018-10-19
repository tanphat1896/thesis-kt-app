package com.ntphat.thesisk40.util

import android.content.Context
import com.ntphat.thesisk40.constant.App

class SharedPref {
    companion object {
        @JvmStatic
        fun store(context: Context, key: String, value: Any): Boolean {
            val pref = context.getSharedPreferences(App.APP_NAME_CODE, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(key, value.toString())
            return editor.commit()
        }

        @JvmStatic
        fun get(context: Context, key: String): String {
            val pref = context.getSharedPreferences(App.APP_NAME_CODE, Context.MODE_PRIVATE)
            return pref.getString(key, "")
        }
    }
}