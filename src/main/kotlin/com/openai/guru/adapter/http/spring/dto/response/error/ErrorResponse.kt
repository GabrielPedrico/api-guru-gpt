package com.openai.guru.adapter.http.spring.dto.response.error

import com.fasterxml.jackson.annotation.JsonProperty
import com.openai.guru.adapter.http.spring.dto.UserDto
import java.time.LocalDateTime
import java.util.*

data class ErrorResponse(
    var id: String? = null,
    var timestamp: LocalDateTime? = null,
    val message: String?,
    @JsonProperty("status_code")
    val statusCode: Int?,
    val endpoint: String?,
    val user: UserDto?
) {
    init {
        if (id == null) {
            id = UUID.randomUUID().toString()
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now()
        }
    }
}
