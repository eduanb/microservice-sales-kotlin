package com.eduanbekker.account

import com.eduanbekker.account.domain.Account
import com.eduanbekker.account.domain.AccountRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.support.GenericMessage

/**
 * This is a integration test that tests everything on the Spring level.
 * This tests starts up the Spring application context with the
 * Spring Cloud Stream testing support enabled and all JPA features disabled.
 * It sends JSON and checks the output JSON, verifying that the JSON converters work
 * and the does what is expected.
 */
@SpringBootTest(properties = ["seed=false"])
@Import(value = [TestChannelBinderConfiguration::class])
@EnableAutoConfiguration(
    exclude = [DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class
    ]
)
class AccountChangeTests {

    @Autowired
    private lateinit var input: InputDestination

    @Autowired
    private lateinit var output: OutputDestination

    @MockkBean
    private lateinit var accountRepository: AccountRepository

    @BeforeEach
    fun init() {
        every { accountRepository.findByIdOrNull(1) } returns Account(1, 0.0)
        every { accountRepository.findByIdOrNull(2) } returns null
        every { accountRepository.save(any()) } answers { args[0] as Account }
    }

    @ParameterizedTest
    @MethodSource("source")
    fun happyPath(request: String, expected: Double) {
        //When
        input.send(GenericMessage(request, mapOf("content-type" to "application/json")))

        //Then
        verify(exactly = 1) { accountRepository.save(Account(1, expected)) }
        val output = String(output.receive().payload)
        JSONAssert.assertEquals("""{"id":1,"balance":$expected}""", output, false)
    }

    @Test
    fun accountNotFound() {
        //Given
        val body = """{"id": 2, "amount": 10.0, "type": "ADD"}"""

        //When
        input.send(GenericMessage(body, mapOf("content-type" to "application/json")))

        //Then
        verify(exactly = 0) { accountRepository.save(any()) }
        Assertions.assertEquals("Could not find account: 2", String(output.receive().payload))
    }

    companion object {
        @JvmStatic
        fun source() = listOf<Arguments>(
            Arguments.of("""{"id": 1, "amount": 10.0, "type": "ADD"}""", 10.0),
            Arguments.of("""{"id": 1, "amount": 20.0, "type": "ADD"}""", 20.0),
            Arguments.of("""{"id": 1, "amount": 10.0, "type": "SUBTRACT"}""", -10.0),
        )
    }


}
