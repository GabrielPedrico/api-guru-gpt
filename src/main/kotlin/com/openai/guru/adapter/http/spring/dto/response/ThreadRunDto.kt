package com.openai.guru.adapter.http.spring.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

data class ThreadRunDto(
    val id: String,
    @JsonProperty("object")
    val objectType: String,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    val created_at: Long,
    val assistant_id: String,
    val thread_id: String,
    val status: String,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    val started_at: Long?,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    val expires_at: Long?,
    val cancelled_at: Long?,
    val failed_at: Long?,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    val completed_at: Long?,
    val last_error: String?,
    val model: String,
    val instructions: String,
    val tools: List<ToolsDto>,
    val file_ids: List<String>,
    val metadata: Map<String, Any>
)

data class ToolsDto(
    val type: String
)