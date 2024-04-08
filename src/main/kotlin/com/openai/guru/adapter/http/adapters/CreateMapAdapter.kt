package com.openai.guru.adapter.http.adapters

import com.openai.guru.adapter.datastore.repositories.UserRepository
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.core.exceptions.UserNotFoundException
import com.openai.guru.core.model.ThreadResponseModel
import com.openai.guru.core.port.out.CreateNumerologyMapPortOut
import com.openai.guru.core.port.out.SendMessageGptPortOut
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateMapAdapter(
    val repository: UserRepository,
    val gptSender: SendMessageGptPortOut
) : CreateNumerologyMapPortOut {

    override fun createMap(userId: UUID): ThreadResponseModel {
        val user = repository.findById(userId.toString()).orElseThrow {
            UserNotFoundException(ErrorResponse(message = "Usuário não encontrado com ID: $userId"))
        }

        return gptSender.createNumerologyMap(user).run {
            ThreadResponseModel(threadId = threadId, createdAt = createdAt, status = status)
        }
    }
}