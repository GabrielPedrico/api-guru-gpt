package com.openai.guru.core.port.out

import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto

fun interface SendGptPortOut {
    fun createNumerologyMap(
        userResponse: UserDto,
        correlationId: String,
    ): ThreadResponseDto
}
