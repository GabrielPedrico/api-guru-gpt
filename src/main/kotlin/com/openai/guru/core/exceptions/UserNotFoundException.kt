package com.openai.guru.core.exceptions

import com.openai.guru.adapter.http.spring.dto.response.ErrorResponse
import org.springframework.http.HttpStatus

class UserNotFoundException(errorReponse: ErrorResponse) : HttpException(errorReponse) {

    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.NOT_FOUND
    }
}
