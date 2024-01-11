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
class CreateMapAdapter (val repository: UserRepository,
                        val gptSender: SendMessageGptPortOut) : CreateNumerologyMapPortOut {

    override fun createMap(userId: UUID):ThreadResponseModel {
        var user = repository.findById(userId.toString()).orElseThrow { UserNotFoundException(ErrorResponse(null,null,"Usuário não encontrado com ID: $userId",null,null,null)) }
        var response = gptSender.createNumerologyMap(user)
        return ThreadResponseModel(response.threadId,response.created_at,response.status)

    }
}