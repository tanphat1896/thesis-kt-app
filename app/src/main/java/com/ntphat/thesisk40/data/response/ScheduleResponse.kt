package com.ntphat.thesisk40.data.response

import com.ntphat.thesisk40.data.Response
import com.ntphat.thesisk40.entity.Schedule

data class ScheduleResponse(
        val content: List<Schedule>
) : Response()