package com.openai.guru.core.port.out

import com.openai.guru.core.model.ThreadResponseModel
import java.util.UUID

fun interface CreateNumerologyMapPortOut {
    fun createMap(
        userId: UUID,
        correlationId: String,
    ): ThreadResponseModel
}
