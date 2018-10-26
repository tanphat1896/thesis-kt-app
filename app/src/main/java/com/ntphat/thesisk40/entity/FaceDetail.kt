package com.ntphat.thesisk40.entity

data class FaceDetail(
        val link: String = "",
        val confidence: Float = 0.0f,
        val student: Student
)