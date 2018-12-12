package com.ntphat.thesisk40.data.response

import com.ntphat.thesisk40.data.Response
import com.ntphat.thesisk40.entity.StudentFace

data class StudentFaceResponse(
        val content: List<StudentFace>
) : Response()