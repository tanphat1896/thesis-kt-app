package com.ntphat.thesisk40.util

import android.annotation.SuppressLint
import com.ntphat.thesisk40.constant.App
import java.text.SimpleDateFormat
import java.util.*

class Environment {
    companion object {
        fun isInProduction(): Boolean {
            return true//App.PROPERTIES["app.env"] == "production"
        }

        fun todayDmy(): String {
            val cal = Calendar.getInstance()
            return """
                ${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.YEAR)}
            """.trimIndent()
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDate(
                date: Any,
                formatOutput: String = "yyyy-MM-dd",
                formatInput: String = "dd-MM-yyyy"
        ): String {
            var d = date
            if (date is String) {
                d = SimpleDateFormat(formatInput).parse(date)
            }
            return SimpleDateFormat(formatOutput).format(d)
        }
    }
}