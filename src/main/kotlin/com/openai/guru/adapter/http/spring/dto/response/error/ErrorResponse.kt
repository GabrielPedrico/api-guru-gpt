package com.openai.guru.adapter.http.spring.dto.response.error

import com.fasterxml.jackson.annotation.JsonProperty
import com.openai.guru.adapter.http.spring.dto.UserDto
import java.time.LocalDateTime
import java.util.*

data class ErrorResponse(
    var id: String? = UUID.randomUUID().toString(),
    var timestamp: LocalDateTime? = LocalDateTime.now(),
    val message: String? = null,
    @JsonProperty("status_code")
    val statusCode: Int? = null,
    val endpoint: String? = null,
    val user: UserDto? = null
)
