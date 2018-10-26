package com.ntphat.thesisk40.model

import java.io.File

interface FaceCheckingModel {
    fun check(teachingClassId: Int, file: File)
}