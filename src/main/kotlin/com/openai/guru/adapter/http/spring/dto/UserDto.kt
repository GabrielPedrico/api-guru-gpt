package com.openai.guru.adapter.http.spring.dto

import java.time.LocalDate

data class UserDto(
    val name: String?,
    val lastname:String?,
    val birthday:LocalDate?
)
