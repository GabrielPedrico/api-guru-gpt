package com.openai.guru.adapter.http.spring.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

class ThreadResponseDto (@JsonProperty("thread_id")
                         val threadId: String?,
                         @JsonFormat(shape = JsonFormat.Shape.NUMBER)
                         val created_at: Long?,
                         val status: String?){
}