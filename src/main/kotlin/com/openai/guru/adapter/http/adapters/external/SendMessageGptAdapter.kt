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
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class SendMessageGptAdapter (val properties:OpenAIProperties,
                             val endpoint:String = "/runs",
                             val restTemplate:RestTemplate = RestTemplate()) : SendMessageGptPortOut {
    override fun createNumerologyMap(user: UserEntity) :ThreadResponseDto {
        val (endpoint, headers, requestBody) = createRequest(user)
        val requestEntity = HttpEntity(requestBody, headers)
        val responseEntity = restTemplate.postForEntity(endpoint, requestEntity, ThreadRunDto::class.java)
        if(!responseEntity.statusCode.is2xxSuccessful) throw SendGPTException(ErrorResponse(null,null,"Não foi possível realizar chamada ao OPENAI",responseEntity.statusCode.value(),null,UserDto(user.id,user.name,user.email)))
        return ThreadResponseDto(responseEntity.body?.threadId,responseEntity.body?.createdAt,responseEntity.body?.status)
    }

    private fun createRequest(user: UserEntity): Triple<String, HttpHeaders, String> {
        val endpoint = properties.api + endpoint
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("OpenAI-Beta", properties.header)
        headers.set("Authorization", properties.token)
        val objectMapper = ObjectMapper().findAndRegisterModules()
        val requestBody = objectMapper.writeValueAsString(
            mapOf(
                "assistant_id" to properties.assistant,
                "thread" to mapOf(
                    "messages" to listOf(
                        mapOf(
                            "role" to "user",
                            "content" to "Nome Completo: " + user.name + " " + user.lastname + " , Data Nascimento: " + user.birthday + " , De acordo com suas instruções monte o mapa numerologico desse individuo com as informações dadas entrando em detalhes profundos de suas características, ao final reuna todas as informações em unico texto que detalha todos os pontos do mapa numerologico com insights personalizados e direcionados para esse individuo conforme suas instruções de execução."
                        )
                    )
                )
            )
        )
        return Triple(endpoint, headers, requestBody)
    }
}