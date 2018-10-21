package com.ntphat.thesisk40.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.entity.Student
import kotlinx.android.synthetic.main.student_item.view.*

class StudentAdapter(
        private val ctx: Context,
        private val vhStudents: MutableList<StudentViewHolder>
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    data class StudentViewHolder(
            val student: Student,
            var present: Boolean = false
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.student_item, parent, false)
        val holder = ViewHolder(view)
        holder.setIsRecyclable(false)
        return holder
    }

    override fun getItemCount(): Int {
        return vhStudents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vhStudent = vhStudents[position]
        holder.genderImg.setImageResource(if (vhStudent.student.gender > 0) {
            R.drawable.female
        } else {
            R.drawable.male
        })
        holder.codeName.text = "${vhStudent.student.studentCode} - ${vhStudent.student.name}"
        holder.image.setImageResource(if (vhStudent.present) {
            R.drawable.ic_check_circle_green_24dp
        } else {
            0
        })

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val codeName: TextView = view.code_name
        val image: ImageView = view.status
        val genderImg: ImageView = view.gender_img
    }
}