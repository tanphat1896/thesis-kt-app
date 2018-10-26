package com.ntphat.thesisk40.entity

data class FaceCheckingResult(
        val totalFace: Int = 0,
        val totalFound: Int = 0,
        val imageLink: String = "",
        val imageDetail: List<FaceDetail> = listOf()
)