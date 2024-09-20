package com.openai.guru.adapter.http.adapters.external

import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadRunDto
import com.openai.guru.core.exceptions.IntegrationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doReturn
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate
import java.util.UUID

@TestPropertySource(
    properties = [
        "app.openai=https://dummy-tests-openai/v1/threads/",
        "app.assistant=asst_LIoCauDKaPo7YBDIonAKXg91",
        "app.token=Bearer dummytoken123456",
        "app.header=dummyheader",
        "app.payment=https://amazon-gurupayment-api/",
    ],
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMessageGptAdapterTest {
    @SpyBean
    private lateinit var sendMessageGptAdapter: SendGptAdapter

    @Test
    fun `test sendGptMessage success`() {
        /** (Scenario setup) **/
        val user =
            UserDto(
                name = "Gabriel",
                lastname = "Pedrico",
                birthday = LocalDate.of(1990, 1, 1),
            )

        /** (Mocking setup) **/
        val mockResponse =
            ResponseEntity.ok(
                ThreadRunDto(
                    threadId = "thread_gdTBaeDCwRrjAOBI0C5vNCsQ",
                    createdAt = 1704608683L,
                    status = "queued",
                ),
            )
        doReturn(mockResponse).`when`(sendMessageGptAdapter).callOpenAI(user)

        /** (Exercise) **/
        val response: ThreadResponseDto = sendMessageGptAdapter.createNumerologyMap(user, UUID.randomUUID().toString())

        /** (Validations) **/
        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.threadId, "thread_gdTBaeDCwRrjAOBI0C5vNCsQ")
        Assertions.assertEquals(response.status, "queued")
        Assertions.assertEquals(response.createdAt, 1704608683L)
    }

    @Test
    fun `test sendGptMessage error scenario`() {
        /** (Scenario setup) **/
        val user =
            UserDto(
                name = "Gabriel",
                lastname = "Pedrico",
                birthday = LocalDate.of(1990, 1, 1),
            )

        /** (Validations) **/
        assertThrows<IntegrationException> {
            sendMessageGptAdapter.createNumerologyMap(user, UUID.randomUUID().toString())
        }
    }
}
