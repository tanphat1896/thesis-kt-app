package com.ntphat.thesisk40.model

interface AttendanceModel {
    fun fetchNextSession(teachingClassId: Int)

    fun save(teachingClassId: Int, date: String, session: Int, studentIds: List<Int>, studentImgs: Map<String, String>)
}