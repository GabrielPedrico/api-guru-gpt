package com.openai.guru.core.exceptions

import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import org.springframework.http.HttpStatus

class IntegrationException(errorReponse: ErrorResponse): HttpException(errorReponse) {
    override fun getHttpStatus(): HttpStatus =
        HttpStatus.valueOf(getErrorResponse().statusCode ?: HttpStatus.SERVICE_UNAVAILABLE.value())
}
