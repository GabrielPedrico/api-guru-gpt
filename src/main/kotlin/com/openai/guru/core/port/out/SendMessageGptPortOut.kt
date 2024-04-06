package com.openai.guru.core.port.out

import com.openai.guru.adapter.datastore.entities.UserEntity
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto

fun interface SendMessageGptPortOut {

    fun createNumerologyMap(user : UserEntity): ThreadResponseDto
}