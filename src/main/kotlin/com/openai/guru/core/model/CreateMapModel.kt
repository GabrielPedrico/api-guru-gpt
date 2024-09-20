package com.openai.guru.core.model

import java.util.UUID

data class CreateMapModel(
    val userId: UUID,
    val correlationId: String,
)
