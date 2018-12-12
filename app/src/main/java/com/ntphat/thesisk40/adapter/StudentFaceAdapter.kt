package com.ntphat.thesisk40.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.entity.StudentFace
import com.ntphat.thesisk40.util.UrlParser

class StudentFaceAdapter(
        private val ctx: Context,
        private val studentFaces: MutableList<StudentFace>
) : RecyclerView.Adapter<StudentFaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.student_face_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentFaces.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentFace = studentFaces[position]
        Glide.with(ctx)
                .load(Uri.parse(UrlParser.getFullApiUrl(studentFace.previewUrl.drop(1))))
                .into(holder.imageView)
    }

    class ViewHolder(
            view: View,
            val imageView: ImageView = view.findViewById(R.id.img_face)
    ) : RecyclerView.ViewHolder(view)
}