package com.openai.guru.adapter.http.adapters.external

import com.fasterxml.jackson.databind.ObjectMapper
import com.openai.guru.adapter.datastore.entities.UserEntity
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
        "Full Name: ${user.name} ${user.lastname}, Date of Birth: ${user.birthday}. According to your instructions, construct the numerological map of this individual with the given information, delving into deep details of their characteristics. In the end, gather all the information into a single text that details all points of the numerological map with personalized and targeted insights for this individual as per your execution instructions."

    private fun handleResponse(responseEntity: ResponseEntity<ThreadRunDto>): ThreadResponseDto =
        with(responseEntity) {
            ThreadResponseDto(
                body?.threadId,
                body?.createdAt,
                body?.status
            )
        }
}