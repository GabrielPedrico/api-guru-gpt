package com.openai.guru.adapter.http.adapters.external

import com.fasterxml.jackson.databind.ObjectMapper
import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadRunDto
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.config.properties.Properties
import com.openai.guru.core.exceptions.IntegrationException
import com.openai.guru.core.port.out.SendGptPortOut
import org.slf4j.Logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class SendGptAdapter(
    private val properties: Properties,
    private val restTemplate: RestTemplate = RestTemplate(),
    private val log: Logger,
) : SendGptPortOut {
    private val openaiEndpoint = properties.openai + "/runs"

    override fun createNumerologyMap(
        userResponse: UserDto,
        correlationId: String,
    ): ThreadResponseDto {
        val responseEntity =
            runCatching {
                callOpenAI(userResponse)
            }.getOrElse { exception ->
                log.error("Error while attempt to communicate with guru-openai-api URL:$openaiEndpoint ASSISTANT:${properties.assistant}")
                throw IntegrationException(ErrorResponse(message = "Error while communicating with OPENAI: ${exception.message}"))
            }
        return handleResponse(responseEntity)
    }

    fun callOpenAI(user: UserDto): ResponseEntity<ThreadRunDto> {
        val requestEntity = createOpenAIRequest(user)
        return restTemplate.postForEntity(openaiEndpoint, requestEntity, ThreadRunDto::class.java)
    }

    fun createOpenAIRequest(user: UserDto): HttpEntity<String> {
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                add("OpenAI-Beta", properties.header)
                add("Authorization", properties.token)
            }

        val requestBody =
            ObjectMapper().findAndRegisterModules().writeValueAsString(
                mapOf(
                    "assistant_id" to properties.assistant,
                    "thread" to
                        mapOf(
                            "messages" to
                                listOf(
                                    mapOf(
                                        "role" to "user",
                                        "content" to createContentJsonOpenAI(user),
                                    ),
                                ),
                        ),
                ),
            )

        return HttpEntity(requestBody, headers)
    }

    private fun createContentJsonOpenAI(user: UserDto) =
        "Full Name: ${user.name} ${user.lastname}," +
            " Date of Birth: ${user.birthday}. " +
            "According to your instructions, construct the numerological map of this individual with the given information," +
            " delving into deep details of their characteristics. In the end, gather all the information into a single text" +
            " that details all points of the numerological map with personalized and targeted insights for this individual " +
            "as per your execution instructions."

    private fun handleResponse(responseEntity: ResponseEntity<ThreadRunDto>): ThreadResponseDto =
        with(responseEntity) {
            ThreadResponseDto(
                body?.threadId,
                body?.createdAt,
                body?.status,
            )
        }
}
