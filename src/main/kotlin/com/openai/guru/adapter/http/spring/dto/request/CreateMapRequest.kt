package com.openai.guru.adapter.http.spring.dto.request

import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated
import java.util.UUID

@Validated
data class CreateMapRequest(@NotNull val userId: UUID)
