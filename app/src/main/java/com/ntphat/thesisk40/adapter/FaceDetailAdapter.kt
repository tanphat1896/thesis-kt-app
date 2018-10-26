package com.ntphat.thesisk40.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.entity.FaceDetail
import com.ntphat.thesisk40.util.UrlParser
import kotlinx.android.synthetic.main.face_detail_item.view.*

class FaceDetailAdapter(
        private val ctx: Context,
        private val faceDetails: MutableList<FaceDetail>
) : RecyclerView.Adapter<FaceDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.face_detail_item, parent, false)
        val holder = ViewHolder(view)
        holder.setIsRecyclable(false)
        return holder
    }

    override fun getItemCount(): Int {
        return faceDetails.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faceDetail = faceDetails[position]
        Glide.with(ctx)
                .load(Uri.parse(UrlParser.getFullApiUrl(faceDetail.link.drop(1))))
                .into(holder.image)
        holder.code.text = faceDetail.student.studentCode
        holder.name.text = faceDetail.student.name
        holder.confidence.text = "%.2f".format((faceDetail.confidence * 100)) + "%"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val code: TextView = view.txt_code
        val name: TextView = view.txt_name
        val confidence: TextView = view.txt_confidence
        val image: ImageView = view.face_img
    }
}