package com.openai.guru.adapter.http.spring.controller


import com.openai.guru.adapter.http.adapters.external.SendGptAdapter
import com.openai.guru.adapter.http.adapters.external.SendPaymentAdapter
import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.request.CreateMapRequest
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.core.exceptions.IntegrationException
import com.openai.guru.core.exceptions.UserNotElegibleException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
import java.util.UUID

@TestPropertySource(properties = [
    "app.openai=https://dummy-tests-openai/v1/threads/",
    "app.assistant=asst_LIoCauDKaPo7YBDIonAKXg91",
    "app.token=Bearer dummytoken123456",
    "app.header=dummyheader",
    "app.payment=https://amazon-gurupayment-api/"
])
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CreateNumerologyMapControllerTest @Autowired constructor(
    private val restTemplate: TestRestTemplate
) {

    @MockBean
    private lateinit var sendMessageGptAdapter: SendGptAdapter
    @MockBean
    private lateinit var sendPaymentAdapter: SendPaymentAdapter

    @Test
    fun `test createNumerologyMap success`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()
        val request = CreateMapRequest(userId = userId)
        val userResponse = UserDto(name = "John", lastname = "Doe", birthday = LocalDate.of(1994,5,10))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-CORRELATION-ID", correlationId)

        /** (Mocking setup) **/
        `when`(sendPaymentAdapter.isUserEligibleForCreateMap(userId, correlationId)).thenReturn(userResponse)
        `when`(sendMessageGptAdapter.createNumerologyMap(userResponse, correlationId)).thenReturn(ThreadResponseDto("thread_gdTBaeDCwRrjAOBI0C5vNCsQ",1704608683L,"queued"))

        /** (Exercise) **/
        val response: ResponseEntity<String> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            String::class.java
        )
        val responseBody = response.body

        /** (Validations) **/
        assertEquals(HttpStatus.ACCEPTED, response.statusCode)
        assertEquals(responseBody,"{\"thread_id\":\"thread_gdTBaeDCwRrjAOBI0C5vNCsQ\",\"created_at\":1704608683,\"status\":\"queued\"}")
    }

    @Test
    fun `test createNumerologyMap error user not elegible payment required`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()
        val request = CreateMapRequest(userId = userId)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-CORRELATION-ID", correlationId)

        /** (Mocking setup) **/
        `when`(sendPaymentAdapter.isUserEligibleForCreateMap(userId,correlationId)).thenThrow(
            UserNotElegibleException(
                ErrorResponse(message = "User John Doe not eligible for create a map, payment is required")
            )
        )
        /** (Exercise) **/
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            ErrorResponse::class.java
        )

        /** (Validations) **/
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNotNull(response.body)
        with(response.body){
            assertNotNull(id)
            assertEquals("User John Doe not eligible for create a map, payment is required", message)
            assertEquals("{POST}/numerology_map", endpoint)
        }
    }

    @Test
    fun `test createNumerologyMap error user not found`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()
        val request = CreateMapRequest(userId = userId)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-CORRELATION-ID", correlationId)

        /** (Mocking setup) **/
        `when`(sendPaymentAdapter.isUserEligibleForCreateMap(userId,correlationId)).thenThrow(
            IntegrationException(
                ErrorResponse(message = "user_id: $userId not found", statusCode = 404)
            )
        )
        /** (Exercise) **/
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            ErrorResponse::class.java
        )

        /** (Validations) **/
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNotNull(response.body)
        with(response.body){
            assertNotNull(id)
            assertEquals("user_id: $userId not found", message)
            assertEquals("{POST}/numerology_map", endpoint)
        }
    }

    @Test
    fun `test createNumerologyMap error whiling attempt to connect with gpt`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()
        val request = CreateMapRequest(userId = userId)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-CORRELATION-ID", correlationId)

        /** (Mocking setup) **/
        `when`(sendPaymentAdapter.isUserEligibleForCreateMap(userId,correlationId)).thenThrow(
            IntegrationException(
                ErrorResponse(message = "Error whiling attempt to connect with openai" )
            )
        )
        /** (Exercise) **/
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            ErrorResponse::class.java
        )

        /** (Validations) **/
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.statusCode)
        assertNotNull(response.body)
        with(response.body){
            assertNotNull(id)
            assertEquals("Error whiling attempt to connect with openai", message)
            assertEquals("{POST}/numerology_map", endpoint)
        }
    }

    @Test
    fun `test createNumerologyMap InvalidRequestError`() {
        /** (Scenario setup) **/
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("correlation_id", UUID.randomUUID().toString())

        /** (Exercise) **/
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(null,headers),
            ErrorResponse::class.java
        )

        /** (Validations) **/
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNotNull(response.body)
        with(response.body){
            assertNotNull(id)
        }
    }

    @Test
    fun `test createNumerologyMap Unexpected Error Default Treatment`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()
        val request = CreateMapRequest(userId = userId)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-CORRELATION-ID", correlationId)

        /** (Mocking setup) **/
        `when`(sendPaymentAdapter.isUserEligibleForCreateMap(userId,correlationId)).thenThrow(
            IllegalArgumentException("Unexpected Error =/")
        )
        /** (Exercise) **/
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            URI("/numerology_map"),
            HttpMethod.POST,
            HttpEntity(request,headers),
            ErrorResponse::class.java
        )

        /** (Validations) **/
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertNotNull(response.body)
        with(response.body){
            assertNotNull(id)
            assertEquals("Unexpected Error =/", message)
            assertEquals("{POST}/numerology_map", endpoint)
        }

    }
}

