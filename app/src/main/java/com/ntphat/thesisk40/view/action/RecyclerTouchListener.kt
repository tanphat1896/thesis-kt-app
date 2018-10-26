package com.ntphat.thesisk40.view.action

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent
import android.view.GestureDetector
import android.view.View


class RecyclerTouchListener : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector
    private val clickListener: ClickListener?

    constructor(context: Context, recyclerView: RecyclerView, clickListener: ClickListener?) {
        this.clickListener = clickListener
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }
}