package com.openai.guru.adapter.http.spring.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class PaymentResponse(
    @JsonProperty("user_id")
    val userId: String?,
    val name: String?,
    val lastname: String?,
    val birthday:LocalDate?,
    var elegebility: Boolean = false
)