package com.ntphat.thesisk40.data

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.Sink
import okio.ForwardingSink
import java.io.IOException
import okio.Okio

class ProgressRequestBody(
        private val requestBody: RequestBody,
        val listener: Listener
) : RequestBody() {

    private lateinit var countingSink: CountingSink

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {
        countingSink = CountingSink(sink)
        val bufferedSink = Okio.buffer(countingSink)

        requestBody.writeTo(bufferedSink)

        bufferedSink.flush()
    }

    private inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {

        private var bytesWritten: Long = 0

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)

            bytesWritten += byteCount
            listener.onRequestProgress(bytesWritten, contentLength())
        }

    }

    interface Listener {
        fun onRequestProgress(bytesWritten: Long, contentLength: Long)
    }
}