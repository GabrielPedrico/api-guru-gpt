package com.openai.guru.adapter.http.spring.controller

import com.openai.guru.adapter.broker.delivery.NumerologyMapService
import com.openai.guru.adapter.http.spring.dto.request.CreateMapRequest
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import org.slf4j.Logger
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Service
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class CreateNumerologyMapController(val service: NumerologyMapService,
                                    val log:Logger) {

    @PostMapping("/numerology_map")
    fun createNumerologyMap(
        @RequestHeader("X-Correlation-ID") correlationId: String,
        @RequestBody request: CreateMapRequest
    ): ResponseEntity<ThreadResponseDto> {
        log.info("Starting GURU-GPT for Resource /numerology_map request:${request}")
        return service.createMap(request,correlationId).also { log.info("Requested a numerology map for user_id:${request.userId}") }
    }
}