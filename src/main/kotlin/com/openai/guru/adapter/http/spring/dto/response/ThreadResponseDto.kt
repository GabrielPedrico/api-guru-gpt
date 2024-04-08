package com.openai.guru.adapter.http.spring.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

data class ThreadResponseDto(
    @JsonProperty("thread_id")
    val threadId: String?,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("created_at")
    val createdAt: Long?,
    val status: String?
)