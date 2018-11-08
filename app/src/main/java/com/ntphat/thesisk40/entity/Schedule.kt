package com.ntphat.thesisk40.entity

data class Schedule(
        val lab: String,
        val timeStart: Int,
        val timeEnd: Int,
        val teachingClass: TeachingClass
)