package com.ntphat.thesisk40.data.response

import com.ntphat.thesisk40.data.Response
import com.ntphat.thesisk40.entity.Student

data class StudentResponse(
        val content: List<Student>
) : Response()