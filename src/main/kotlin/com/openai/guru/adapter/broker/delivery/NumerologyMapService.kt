package com.openai.guru.adapter.broker.delivery

import com.openai.guru.adapter.http.spring.dto.request.CreateMapRequest
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.core.port.`in`.CreateNumerologyMapPortIn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class NumerologyMapService(
    val createNumerologyMapPortIn: CreateNumerologyMapPortIn
) {

    fun createMap(request: CreateMapRequest): ResponseEntity<ThreadResponseDto> {
        val response = createNumerologyMapPortIn.createNumerologyMap(request.userId).let {
            ResponseEntity.status(HttpStatus.CREATED).body(ThreadResponseDto(it.threadId, it.createdAt, it.status))
        }

        return response
    }
}