package com.openai.guru.adapter.http.spring.controller

import com.openai.guru.adapter.broker.delivery.NumerologyMapService
import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.request.CreateMapRequest
import com.openai.guru.adapter.http.spring.dto.response.error.ErrorResponse
import com.openai.guru.core.exceptions.SendGPTException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime
import java.util.*

@TestPropertySource(properties = ["openai.api=https://dummy-tests-openai/v1/threads/",
                                  "openai.assistant=asst_LIoCauDKaPo7YBDIonAKXg91",
                                  "openai.token=Bearer dummytoken123456",
                                  "openai.header=dummyheader"

])
class CreateNumerologyMapControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var numerologyMapService: NumerologyMapService

    @InjectMocks
    private lateinit var createNumerologyMapController: CreateNumerologyMapController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(createNumerologyMapController).build()
    }

    @Test
    fun testCreateNumerologyMapError() {
        // Given
        val correlationId = "correlationId"
        val request = CreateMapRequest(UUID.fromString("f4c90c33-07f6-4e83-8dfe-bbb536d9e25e"))

        `when`(numerologyMapService.createMap(request)).thenThrow(SendGPTException(createErrorResponse()))

        // When & Then
        assertThrows(SendGPTException::class.java) {
            createNumerologyMapController.createNumerologyMap(correlationId, request)
        }
    }

    private fun createErrorResponse(): ErrorResponse {
        return ErrorResponse(
            id = "errorId",
            timestamp = LocalDateTime.now(),
            message = "Error occurred",
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            endpoint = "/api/createNumerologyMap",
            user = UserDto(id = "userId", name = "John Doe", email = "john@example.com")
        )
    }
}