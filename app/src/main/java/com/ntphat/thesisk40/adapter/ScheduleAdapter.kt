package com.ntphat.thesisk40.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.entity.Schedule

class ScheduleAdapter(
        private val ctx: Context,
        private val schedules: MutableList<Schedule>
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.schedule_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = schedules[position]
        val teachingClass = schedule.teachingClass

        holder.codeNameGroup.text = "${teachingClass.subjectCode} - ${teachingClass.name} - N${teachingClass.group}"
        holder.time.text = "Tiết: ${schedule.timeStart} - ${schedule.timeEnd}"
        holder.lab.text = "Phòng: ${schedule.lab}"
    }

    class ViewHolder(
            view: View,
            val codeNameGroup: TextView = view.findViewById(R.id.code_name_group),
            val time: TextView = view.findViewById(R.id.time),
            val lab: TextView = view.findViewById(R.id.lab)
    ) : RecyclerView.ViewHolder(view)
}