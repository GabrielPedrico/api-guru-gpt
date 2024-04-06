package com.openai.guru.adapter.http.spring.controller

import com.openai.guru.adapter.datastore.entities.UserEntity
import com.openai.guru.adapter.datastore.repositories.UserRepository
import com.openai.guru.adapter.http.adapters.external.SendMessageGptAdapter
import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.request.CreateMapRequest
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.core.exceptions.SendGPTException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.TestPropertySource
import java.net.URI
import java.time.LocalDate
import java.util.*

@TestPropertySource(properties = [
    "openai.api=https://dummy-tests-openai/v1/threads/",
    "openai.assistant=asst_LIoCauDKaPo7YBDIonAKXg91",
    "openai.token=Bearer dummytoken123456",
    "openai.header=dummyheader"
])
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CreateNumerologyMapControllerTest @Autowired constructor(
    private val restTemplate: TestRestTemplate
) {

    @Autowired
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var sendMessageGptAdapter: SendMessageGptAdapter

    @Test
    fun `test createNumerologyMap success`() {

        val user = UserEntity(
            id = "",
            name = "Gabriel",
            lastname = "Pedrico",
            birthday = LocalDate.of(1990, 1, 1),
            email = "pedripedrio@example.com"
        )
        val userCreated = userRepository.save(user)

        val request = CreateMapRequest(userId = UUID.fromString(userCreated.id))

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("correlation_id", UUID.randomUUID().toString())

        `when`(sendMessageGptAdapter.createNumerologyMap(userCreated)).thenReturn(ThreadResponseDto("thread_gdTBaeDCwRrjAOBI0C5vNCsQ",1704608683L,"queued"))

        val response: ResponseEntity<String> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            String::class.java
        )

        val responseBody = response.body

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(responseBody,"{\"thread_id\":\"thread_gdTBaeDCwRrjAOBI0C5vNCsQ\",\"created_at\":1704608683,\"status\":\"queued\"}")
    }

    @Test
    fun `test createNumerologyMap UserNotFoundError`() {

        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            name = "John",
            lastname = "Doe",
            birthday = LocalDate.of(1990, 1, 1),
            email = "john.doe@example.com"
        )

        val request = CreateMapRequest(userId = UUID.fromString(user.id))

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("correlation_id", UUID.randomUUID().toString())

        `when`(sendMessageGptAdapter.createNumerologyMap(user)).thenReturn(ThreadResponseDto("thread_gdTBaeDCwRrjAOBI0C5vNCsQ",1704608683L,"queued"))

        val response: ResponseEntity<String> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            String::class.java
        )

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `test createNumerologyMap SendGptError`() {

        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            name = "John",
            lastname = "Doe",
            birthday = LocalDate.of(1990, 1, 1),
            email = "john.doe@example.com"
        )

        val userCreated = userRepository.save(user)

        val request = CreateMapRequest(userId = UUID.fromString(userCreated.id))

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("correlation_id", UUID.randomUUID().toString())

        `when`(sendMessageGptAdapter.createNumerologyMap(userCreated)).thenThrow(
            SendGPTException(
                ErrorResponse(null,null,"Não foi possível realizar chamada ao OPENAI",null,null,
                    UserDto(user.id,user.name,user.email)
                )
            ))

        val response: ResponseEntity<String> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            String::class.java
        )

        assertEquals(HttpStatus.REQUEST_TIMEOUT, response.statusCode)
    }

    @Test
    fun `test createNumerologyMap InvalidRequestError`() {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("correlation_id", UUID.randomUUID().toString())

        val response: ResponseEntity<String> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(null,headers),
            String::class.java
        )

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `test createNumerologyMap UnexpectErrorDefaultTreament`() {

        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            name = "John",
            lastname = "Doe",
            birthday = LocalDate.of(1990, 1, 1),
            email = "john.doe@example.com"
        )

        val userCreated = userRepository.save(user)

        val request = CreateMapRequest(userId = UUID.fromString(userCreated.id))

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("correlation_id", UUID.randomUUID().toString())

        `when`(sendMessageGptAdapter.createNumerologyMap(user)).thenThrow(
            IllegalArgumentException("Erro inesperado ocorreu no fluxo")
            )

        val response: ResponseEntity<String> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            String::class.java
        )
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
    }
}

