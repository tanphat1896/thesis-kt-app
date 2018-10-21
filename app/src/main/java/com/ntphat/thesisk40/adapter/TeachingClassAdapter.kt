package com.ntphat.thesisk40.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.entity.TeachingClass

class TeachingClassAdapter(
        private val ctx: Context,
        private val teachingClasses: MutableList<TeachingClass>
) : RecyclerView.Adapter<TeachingClassAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.teaching_class_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return teachingClasses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teachingClass = teachingClasses[position]

        holder.codeName.text = "${teachingClass.subjectCode} - ${teachingClass.name}"
        holder.group.text = "Nhóm ${teachingClass.group}"
        holder.totalCheck.text = "(Đã điểm danh ${teachingClass.totalCheck} lần)"
    }

    class ViewHolder(
            view: View,
            val codeName: TextView = view.findViewById(R.id.code_name),
            val group: TextView = view.findViewById(R.id.group),
            val totalCheck: TextView = view.findViewById(R.id.total_check)
    ) : RecyclerView.ViewHolder(view)
}