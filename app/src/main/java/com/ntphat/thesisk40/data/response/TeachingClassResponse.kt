package com.ntphat.thesisk40.data.response

import com.ntphat.thesisk40.data.Response
import com.ntphat.thesisk40.entity.TeachingClass

data class TeachingClassResponse(
        val content: List<TeachingClass>
) : Response()