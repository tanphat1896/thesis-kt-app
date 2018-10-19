package com.ntphat.thesisk40.util

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

class UiWidget {
    companion object {
        @JvmStatic
        fun toast(context: Context, text: String, longTime: Boolean = false) {
            Toast.makeText(
                    context,
                    text,
                    if (longTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            ).show()
        }

        @JvmStatic
        fun snackBar(context: Activity, text: String, longTime: Boolean = false) {
            Snackbar.make(
                    context.window.decorView.rootView,
                    text,
                    if (longTime) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
            ).show()
        }

        @JvmStatic
        fun toggleSoftKeyboard(context: Activity, hidden: Boolean = true, editText: EditText? = null) {
            val view = context.currentFocus ?: return
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hidden) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } else {
                editText?.apply {
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
    }
}