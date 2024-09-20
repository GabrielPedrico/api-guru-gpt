package com.openai.guru.adapter.broker.delivery

import com.openai.guru.adapter.http.spring.dto.request.CreateMapRequest
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.core.model.CreateMapModel
import com.openai.guru.core.port.inbound.CreateNumerologyMapPortIn
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class NumerologyMapService(
    val createNumerologyMapPortIn: CreateNumerologyMapPortIn,
    val log: Logger,
) {
    fun createMap(
        request: CreateMapRequest,
        correlationId: String,
    ): ResponseEntity<ThreadResponseDto> =
        createNumerologyMapPortIn.createNumerologyMap(request.toCreateMapModel(correlationId)).let {
            log.info(
                "Numerology Map Request Created successfully for " +
                    "user_id:${request.userId}\n{thread_id:${it.threadId},\nstatus:${it.status}",
            )
            ResponseEntity.status(HttpStatus.ACCEPTED).body(ThreadResponseDto(it.threadId, it.createdAt, it.status))
        }
}

fun CreateMapRequest.toCreateMapModel(correlationId: String): CreateMapModel {
    return CreateMapModel(
        userId = this.userId,
        correlationId = correlationId,
    )
}
