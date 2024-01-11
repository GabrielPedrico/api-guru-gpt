package com.openai.guru.adapter.http.spring.controller

import com.openai.guru.adapter.broker.delivery.NumerologyMapService
import com.openai.guru.adapter.http.spring.dto.request.CreateMapRequest
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
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
@RequestMapping(produces	=	[MediaType.APPLICATION_JSON_VALUE])
class CreateNumerologyMapController (val service : NumerologyMapService) {

    @PostMapping("/numerology_map")
    fun createNumerologyMap(@RequestHeader("correlation_id") correlationId: String,
                            @RequestBody request : CreateMapRequest): ResponseEntity<ThreadResponseDto>{
        return service.createMap(request)
    }
}