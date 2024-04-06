package com.openai.guru.adapter.http.spring.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

data class ThreadRunDto(
    val id: String,
    @JsonProperty("object")
    val objectType: String,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("created_at")
    val createdAt: Long,
    @JsonProperty("assistant_id")
    val assistantId: String,
    @JsonProperty("thread_id")
    val threadId: String,
    val status: String,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("started_at")
    val startedAt: Long?,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("expires_at")
    val expiresAt: Long?,
    @JsonProperty("cancelled_at")
    val cancelledAt: Long?,
    @JsonProperty("failed_at")
    val failedAt: Long?,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("completed_at")
    val completedAt: Long?,
    @JsonProperty("last_error")
    val lastError: String?,
    val model: String,
    val instructions: String,
    val tools: List<ToolsDto>,
    @JsonProperty("file_ids")
    val fileIds: List<String>,
    val metadata: Map<String, Any>
)

data class ToolsDto(
    val type: String
)