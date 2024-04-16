package com.openai.guru.adapter.http.adapters

import com.openai.guru.core.model.ThreadResponseModel
import com.openai.guru.core.port.out.CreateNumerologyMapPortOut
import com.openai.guru.core.port.out.SendGuruPaymentPortOut
import com.openai.guru.core.port.out.SendGptPortOut
import org.slf4j.Logger
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateMapAdapter(
    val gptSender: SendGptPortOut,
    val paymentSender: SendGuruPaymentPortOut,
    val log: Logger
) : CreateNumerologyMapPortOut {

    override fun createMap(userId: UUID,correlationId:String): ThreadResponseModel {
        log.info("[GURU-GPT][CORRELATION-ID:{$correlationId}]Checking user elegibility...[GURU-GPT]")
        val user = paymentSender.isUserEligibleForCreateMap(userId,correlationId)
        log.info("[GURU-GPT][CORRELATION-ID:{$correlationId}]Elegibility OK! User is able to create a Numerology Map, sending openai request...[GURU-GPT]")
        return gptSender.createNumerologyMap(user,correlationId).run {
            ThreadResponseModel(threadId = threadId, createdAt = createdAt, status = status)
        }
    }
}