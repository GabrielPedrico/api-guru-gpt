package com.openai.guru.adapter.http.adapters.external

import com.openai.guru.adapter.datastore.entities.UserEntity
import com.openai.guru.adapter.http.spring.dto.response.ThreadResponseDto
import com.openai.guru.adapter.http.spring.dto.response.ThreadRunDto
import com.openai.guru.core.exceptions.SendGPTException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doReturn
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate
import java.util.*

@TestPropertySource(
    properties = [
        "openai.api=https://dummy-tests-openai/v1/threads/",
        "openai.assistant=asst_LIoCauDKaPo7YBDIonAKXg91",
        "openai.token=Bearer dummytoken123456",
        "openai.header=dummyheader"
    ]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMessageGptAdapterTest {

    @SpyBean
    private lateinit var sendMessageGptAdapter: SendMessageGptAdapter

    @Test
    fun `test sendGptMessage success`() {
        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            name = "Gabriel",
            lastname = "Pedrico",
            birthday = LocalDate.of(1990, 1, 1),
            email = "pedripedrio@example.com"
        )

        val mockResponse = ResponseEntity.ok(
            ThreadRunDto(
                threadId = "thread_gdTBaeDCwRrjAOBI0C5vNCsQ",
                createdAt = 1704608683L,
                status = "queued"
            )
        )
        val request = createRequest(user)
        doReturn(mockResponse).`when`(sendMessageGptAdapter).callOpenAI(request)

        val response: ThreadResponseDto = sendMessageGptAdapter.createNumerologyMap(user)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.threadId, "thread_gdTBaeDCwRrjAOBI0C5vNCsQ")
        Assertions.assertEquals(response.status, "queued")
        Assertions.assertEquals(response.createdAt, 1704608683L)
    }

    @Test
    fun `test sendGptMessage error scenario`() {
        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            name = "Gabriel",
            lastname = "Pedrico",
            birthday = LocalDate.of(1990, 1, 1),
            email = "pedripedrio@example.com"
        )

        assertThrows<SendGPTException> {
            sendMessageGptAdapter.createNumerologyMap(user)
        }
    }

    private fun createRequest(user: UserEntity): HttpEntity<String> = sendMessageGptAdapter.createRequest(user)
}
