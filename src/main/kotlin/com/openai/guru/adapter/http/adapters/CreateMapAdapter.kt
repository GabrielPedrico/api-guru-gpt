package com.openai.guru.adapter.http.adapters

import com.openai.guru.core.model.ThreadResponseModel
import com.openai.guru.core.port.out.CreateNumerologyMapPortOut
import com.openai.guru.core.port.out.SendGptPortOut
import com.openai.guru.core.port.out.SendGuruPaymentPortOut
import org.slf4j.Logger
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateMapAdapter(
    val gptSender: SendGptPortOut,
    val paymentSender: SendGuruPaymentPortOut,
    val log: Logger,
) : CreateNumerologyMapPortOut {
    override fun createMap(
        userId: UUID,
        correlationId: String,
    ): ThreadResponseModel {
        log.info("Checking user elegibility...")
        val user = paymentSender.isUserEligibleForCreateMap(userId, correlationId)
        log.info("Elegibility OK! User is able to create a Numerology Map, sending openai request...")
        return gptSender.createNumerologyMap(user, correlationId).run {
            ThreadResponseModel(threadId = threadId, createdAt = createdAt, status = status)
        }
    }
}
