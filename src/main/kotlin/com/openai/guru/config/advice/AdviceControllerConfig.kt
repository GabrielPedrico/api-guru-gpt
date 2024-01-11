package com.openai.guru.config.advice

import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.core.exceptions.HttpException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class AdviceControllerConfig(private val request: HttpServletRequest) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(HttpException::class)
    protected fun handleHttpExceptions(throwable: HttpException): ResponseEntity<ErrorResponse> {
        val response = throwable.getErrorResponse()
        return ResponseEntity.status(throwable.getHttpStatus()).body(buildErrorApiResponse(response.message,throwable.getHttpStatus().value(),response.user))
    }

    // Tratamento padrão para outros erros
    @ExceptionHandler(Exception::class)
    protected fun handleException(throwable: Throwable): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(httpStatus).body(buildErrorApiResponse(throwable.message,httpStatus))
    }

    private fun buildErrorApiResponse(
        message: String?,
        httpStatus: HttpStatus,
    ): ErrorResponse {
        return ErrorResponse(null, null,message,httpStatus.value(),getPathUri(),null)
    }

    private fun buildErrorApiResponse(
        message: String?,
        httpStatus: Int?,
        user: UserDto?
    ): ErrorResponse {
        return ErrorResponse(null, null,message,httpStatus,getPathUri(),user)
    }

    private fun getPathUri(): String {
        return getFullRouteRequest()
    }

    fun getVerbMethodRequest(): String {
        return request.method
    }

    fun pathRequestUri(): String {
        return request.requestURI.substring(request.contextPath.length)
    }

    fun getFullRouteRequest(): String {
        return "{${getVerbMethodRequest()}}${pathRequestUri()}"
    }
}