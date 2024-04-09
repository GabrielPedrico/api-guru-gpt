package com.openai.guru.adapter.http.adapters.external

import com.fasterxml.jackson.databind.ObjectMapper
import com.openai.guru.adapter.datastore.entities.UserEntity
import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadRunDto
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.config.properties.OpenAIProperties
import com.openai.guru.core.exceptions.SendGPTException
import com.openai.guru.core.port.out.SendMessageGptPortOut
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class SendMessageGptAdapter(
    private val properties: OpenAIProperties,
    private val restTemplate: RestTemplate = RestTemplate()
) : SendMessageGptPortOut {
    private val endpoint = properties.api + "/runs"

    override fun createNumerologyMap(user: UserEntity): ThreadResponseDto {
        val requestEntity = createRequest(user)
        val responseEntity = runCatching {
            callOpenAI(requestEntity)
        }.getOrElse { exception ->
            // Aqui você lança a exceção personalizada em caso de erro
            throw SendGPTException(ErrorResponse(message = "Error while communicating with OPENAI: ${exception.message}"))
        }
        return handleResponse(responseEntity)
    }

    fun callOpenAI(requestEntity: HttpEntity<String>): ResponseEntity<ThreadRunDto> =
        restTemplate.postForEntity(endpoint, requestEntity, ThreadRunDto::class.java)

    fun createRequest(user: UserEntity): HttpEntity<String> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            add("OpenAI-Beta", properties.header)
            add("Authorization", properties.token)
        }

        val requestBody = ObjectMapper().findAndRegisterModules().writeValueAsString(
            mapOf(
                "assistant_id" to properties.assistant,
                "thread" to mapOf(
                    "messages" to listOf(
                        mapOf(
                            "role" to "user",
                            "content" to createContent(user)
                        )
                    )
                )
            )
        )

        return HttpEntity(requestBody, headers)
    }

    private fun createContent(user: UserEntity) =
        "Nome Completo: ${user.name} ${user.lastname} , Data Nascimento: ${user.birthday} , De acordo com suas instruções monte o mapa numerologico desse individuo com as informações dadas entrando em detalhes profundos de suas características, ao final reuna todas as informações em unico texto que detalha todos os pontos do mapa numerologico com insights personalizados e direcionados para esse individuo conforme suas instruções de execução."

    private fun handleResponse(responseEntity: ResponseEntity<ThreadRunDto>): ThreadResponseDto =
        with(responseEntity) {
            ThreadResponseDto(
                body?.threadId,
                body?.createdAt,
                body?.status
            )
        }
}