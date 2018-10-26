package com.ntphat.thesisk40.data.response

import com.ntphat.thesisk40.data.Response

/**
 * Common response, có thể nhận bất cứ thứ gì rồi sau đó cast ở chỗ consume nó
 */
data class CommonResponse(
        val content: Any? = null
) : Response()