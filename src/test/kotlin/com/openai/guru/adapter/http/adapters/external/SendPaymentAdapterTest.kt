package com.openai.guru.adapter.http.adapters.external

import com.openai.guru.adapter.http.spring.dto.UserDto
import com.openai.guru.adapter.http.spring.dto.response.PaymentResponse
import com.openai.guru.core.exceptions.IntegrationException
import com.openai.guru.core.exceptions.UserNotElegibleException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
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
class SendPaymentAdapterTest {
    @SpyBean
    private lateinit var sendPaymentAdapter: SendPaymentAdapter

    @Test
    fun `test sendPaymentRequest success user elegible`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()

        /** (Mocking setup) **/
        val mockResponse =
            ResponseEntity.ok()
                .body(
                    PaymentResponse(
                        userId = UUID.randomUUID().toString(),
                        name = "Gabriel",
                        lastname = "Pedrico",
                        birthday = LocalDate.of(1990, 1, 1),
                        elegebility = true,
                    ),
                )

        Mockito.doReturn(mockResponse).`when`(sendPaymentAdapter).callGuruPayment(userId, correlationId)

        /** (Exercise) **/
        val response: UserDto = sendPaymentAdapter.isUserEligibleForCreateMap(userId, correlationId)

        /** (Validations) **/
        Assertions.assertNotNull(response)
        with(response) {
            Assertions.assertEquals("Gabriel", name)
            Assertions.assertEquals("Pedrico", lastname)
            Assertions.assertEquals(LocalDate.of(1990, 1, 1), birthday)
        }
    }

    @Test
    fun `test sendPaymentRequest error user not elegible payment required`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()

        /** (Mocking setup) **/
        val mockResponse =
            ResponseEntity.ok()
                .body(
                    PaymentResponse(
                        userId = UUID.randomUUID().toString(),
                        name = "Gabriel",
                        lastname = "Pedrico",
                        birthday = LocalDate.of(1990, 1, 1),
                        elegebility = false,
                    ),
                )

        Mockito.doReturn(mockResponse).`when`(sendPaymentAdapter).callGuruPayment(userId, correlationId)

        /** (Exercise & Validation) **/
        assertThrows<UserNotElegibleException> {
            sendPaymentAdapter.isUserEligibleForCreateMap(userId, correlationId)
        }
    }

    @Test
    fun `test sendPaymentRequest error scenario`() {
        /** (Scenario setup) **/
        val userId = UUID.randomUUID()
        val correlationId = UUID.randomUUID().toString()

        /** (Exercise & Validation) **/
        assertThrows<IntegrationException> {
            sendPaymentAdapter.isUserEligibleForCreateMap(userId, correlationId)
        }
    }
}
