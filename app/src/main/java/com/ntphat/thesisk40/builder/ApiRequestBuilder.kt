package com.ntphat.thesisk40.builder

import com.ntphat.thesisk40.constant.Http
import okhttp3.*
import java.io.File
import java.security.InvalidParameterException

class ApiRequestBuilder {
    private val requestBuilder: Request.Builder = Request.Builder()

    private var headers = mutableMapOf(
            "Accept" to Http.APP_JSON
    )

    private var method: String = "get"

    private lateinit var url: String

    private lateinit var body: RequestBody

    private var hasFile: Boolean = false

    fun method(method: String): ApiRequestBuilder {
        this.method = method.toLowerCase()
        return this
    }

    fun url(url: String): ApiRequestBuilder {
        this.url = url
        return this
    }

    fun headers(headers: MutableMap<String, String>): ApiRequestBuilder {
        this.headers = headers
        return this
    }

    fun header(name: String, value: String): ApiRequestBuilder {
        this.headers[name] = value
        return this
    }

    fun body(body: Map<String, Any>): ApiRequestBuilder {
        this.hasFile = false
        val builder = FormBody.Builder()
        body.forEach { (key, value) -> builder.add(key, value.toString()) }
        this.body = builder.build()
        return this
    }

    /**
     * Chưa implement fun này
     */
    fun file(file: File, extras: Map<String, Any>): ApiRequestBuilder {
        this.hasFile = true

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart(
                "file", file.name, RequestBody.create(Http.MEDIA_JPEG, file)
        )
        extras.forEach { (key, value) -> builder.addFormDataPart(key, value.toString()) }

        this.body = builder.build()

        return this
    }

    @Throws(InvalidParameterException::class)
    fun build(): Request {
        requestBuilder.url(url).headers(Headers.of(headers))
        when (this.method) {
            "post" -> requestBuilder.post(body)
            "put" -> requestBuilder.put(body)
            "delete" -> requestBuilder.delete()
        }

        return requestBuilder.build()
    }
}