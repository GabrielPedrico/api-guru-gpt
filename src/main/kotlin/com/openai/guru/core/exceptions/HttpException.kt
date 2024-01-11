package com.openai.guru.core.exceptions

import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import org.springframework.http.HttpStatus

abstract class HttpException(private val errorResponse: ErrorResponse) : RuntimeException(errorResponse.toString()) {

    abstract fun getHttpStatus(): HttpStatus

    fun getErrorResponse(): ErrorResponse {
        return errorResponse
    }
}

