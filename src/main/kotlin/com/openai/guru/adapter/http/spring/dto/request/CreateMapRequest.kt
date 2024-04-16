package com.openai.guru.adapter.http.spring.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated
import java.util.UUID

@Validated
data class CreateMapRequest(
    @JsonProperty("user_id") @NotNull val userId: UUID
)
