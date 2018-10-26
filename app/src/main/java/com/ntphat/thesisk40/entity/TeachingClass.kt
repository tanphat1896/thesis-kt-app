package com.ntphat.thesisk40.entity

data class TeachingClass(
        val id: Int,
        val subjectCode: String,
        val name: String,
        val group: Int,
        val schoolYear: String,
        val totalCheck: Int = 1
)