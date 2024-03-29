package com.ntphat.thesisk40.constant

object ApiUrl {
    @JvmStatic
    val BASE_PRODUCTION = "http://107.191.61.4"

    @JvmStatic
    val BASE_DEVELOPMENT = "http://192.168.43.162"

    @JvmStatic
    val LOGIN_PATH = "/api/login"

    @JvmStatic
    val GET_TEACHING_CLASS = "/api/v1/teaching_classes"

    @JvmStatic
    val POST_FACE_CHECKING = "/api/v1/face_checking"

    @JvmStatic
    val GET_SCHEDULE = "/api/v1/schedules"

    @JvmStatic
    val GET_STUDENT_FACES = "/api/v1/students"
}