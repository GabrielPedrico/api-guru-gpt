package com.openai.guru.adapter.http.adapters.external

import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.response.PaymentResponse
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.config.properties.Properties
import com.openai.guru.core.exceptions.IntegrationException
import com.openai.guru.core.exceptions.UserNotElegibleException
import com.openai.guru.core.port.out.SendGuruPaymentPortOut
import org.slf4j.Logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.UUID

@Component
class SendPaymentAdapter(
    private val properties: Properties,
    private val restTemplate: RestTemplate = RestTemplate(),
    private val log: Logger
) : SendGuruPaymentPortOut {
    val paymentApi: String = properties.payment + "payment/{userId}/status"
    override fun isUserEligibleForCreateMap(userId: UUID, correlationId: String): UserDto {
        val responseEntity = runCatching {
            callGuruPayment(userId, correlationId)
        }.getOrElse { exception ->
            log.error("[GURU-GPT][CORRELATION-ID:{$correlationId}]Error while attempt to communicate with guru-payment-api URL: ${properties.payment}payment/${userId}/status [GURU-GPT]")
            throw IntegrationException(ErrorResponse(message = "Error while communicating with GURU-PAYMENT-API: ${exception.message}"))
        }
        return handleResponse(responseEntity)
    }

    fun callGuruPayment(user: UUID, correlationId: String): ResponseEntity<PaymentResponse> {
        val url = createGuruPaymentURI(user, correlationId)
        val headers = HttpHeaders().apply {
            set("Content-Type", "application/json")
            set("correlation-id", correlationId)
        }
        val entity = HttpEntity<String>(headers)
        return restTemplate.exchange(url, HttpMethod.GET, entity, PaymentResponse::class.java)
    }


    fun createGuruPaymentURI(userId: UUID, correlationId: String): URI {
        return UriComponentsBuilder
            .fromUriString(paymentApi)
            .buildAndExpand(mapOf("userId" to userId))
            .toUri()
    }

    private fun handleResponse(responseEntity: ResponseEntity<PaymentResponse>): UserDto =
        with(responseEntity) {
            if (responseEntity.body?.elegebility == false) throw UserNotElegibleException(ErrorResponse(message = "User ${responseEntity.body.name} ${responseEntity.body.lastname} not eligible for create a map, payment is required"))
            UserDto(
                body?.name,
                body?.lastname,
                body?.birthday
            )
        }


}