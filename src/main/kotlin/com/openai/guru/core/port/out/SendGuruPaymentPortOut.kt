package com.openai.guru.core.port.out

import com.openai.guru.adapter.http.spring.dto.UserDto
import java.util.*

fun interface SendGuruPaymentPortOut {
    fun isUserEligibleForCreateMap(userId: UUID,correlationId:String): UserDto
}