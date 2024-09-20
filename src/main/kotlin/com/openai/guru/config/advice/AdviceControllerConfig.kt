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
class AdviceControllerConfig(
    private val request: HttpServletRequest,
) : ResponseEntityExceptionHandler() {
    @ExceptionHandler(HttpException::class)
    protected fun handleHttpExceptions(throwable: HttpException): ResponseEntity<ErrorResponse> =
        throwable.getErrorResponse().let {
            ResponseEntity.status(
                throwable.getHttpStatus(),
            ).body(buildErrorApiResponse(it.message, throwable.getHttpStatus().value(), it.user))
        }

    // Tratamento padrão para outros erros
    @ExceptionHandler(Exception::class)
    protected fun handleException(throwable: Throwable): ResponseEntity<ErrorResponse> =
        HttpStatus.INTERNAL_SERVER_ERROR.let {
            ResponseEntity.status(it).body(buildErrorApiResponse(throwable.message, it))
        }

    private fun buildErrorApiResponse(
        message: String?,
        httpStatus: HttpStatus,
    ) = ErrorResponse(message = message, statusCode = httpStatus.value(), endpoint = getPathUri())

    private fun buildErrorApiResponse(
        message: String?,
        httpStatus: Int?,
        user: UserDto?,
    ) = ErrorResponse(message = message, statusCode = httpStatus, endpoint = getPathUri(), user = user)

    private fun getPathUri() = getFullRouteRequest()

    fun getVerbMethodRequest(): String = request.method

    fun pathRequestUri() = request.requestURI.substring(request.contextPath.length)

    fun getFullRouteRequest(): String = "{${getVerbMethodRequest()}}${pathRequestUri()}"
}
