package com.openai.guru.adapter.http.spring.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

data class ThreadRunDto(
    val id: String = "",
    @JsonProperty("object")
    val objectType: String = "",
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("created_at")
    val createdAt: Long,
    @JsonProperty("assistant_id")
    val assistantId: String = "",
    @JsonProperty("thread_id")
    val threadId: String,
    val status: String,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("started_at")
    val startedAt: Long? = null,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("expires_at")
    val expiresAt: Long? = null,
    @JsonProperty("cancelled_at")
    val cancelledAt: Long? = null,
    @JsonProperty("failed_at")
    val failedAt: Long? = null,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("completed_at")
    val completedAt: Long? = null,
    @JsonProperty("last_error")
    val lastError: String? = null,
    val model: String = "",
    val instructions: String = "",
    val tools: List<ToolsDto> = emptyList(),
    @JsonProperty("file_ids")
    val fileIds: List<String> = emptyList(),
    val metadata: Map<String, Any> = emptyMap(),
)

data class ToolsDto(
    val type: String,
)
