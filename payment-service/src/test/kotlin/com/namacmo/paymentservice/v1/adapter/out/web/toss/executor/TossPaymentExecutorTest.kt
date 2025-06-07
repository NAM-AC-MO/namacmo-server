package com.namacmo.paymentservice.v1.adapter.out.web.toss.executor

import com.namacmo.paymentservice.test.PSPTestWebClientConfiguration
import com.namacmo.paymentservice.v1.adapter.out.web.toss.exception.PSPConfirmationException
import com.namacmo.paymentservice.v1.adapter.out.web.toss.exception.TossPaymentError
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.util.*

/**
 * Toss Payments에서는 특정 에러 상황을 테스트할 수 있도록 HTTP Header를 통해 원하는 에러를 반환하는 기능을 제공합니다.
 *
 * 이 테스트 클래스는 테스트 전용 구성 클래스인 [PSPTestWebClientConfiguration]을 사용하여
 * Toss의 PSP(Payment Service Provider) 연동 기능을 보다 쉽게 테스트할 수 있는 환경을 구성합니다.
 *
 * 본 클래스는 [PaymentExecutor]의 Toss 연동 구현체를 테스트하는 데 사용됩니다.
 *
 * @see <a href="https://www.tosspayments.com/blog/articles/dev-7">Toss Payments 개발 블로그 - 테스트 가이드</a>
 */
@SpringBootTest
@Import(PSPTestWebClientConfiguration::class)
@Tag("TooLongTime")
class TossPaymentExecutorTest(@Autowired private val pspTestWebClientConfiguration: PSPTestWebClientConfiguration) {

    @Test
    fun `should handle correctly various TossPaymentError scenarios`() {
        generateErrorScenarios().forEach { errorScenario ->
            val command = PaymentConfirmCommand(
                paymentKey = UUID.randomUUID().toString(),
                orderId = UUID.randomUUID().toString(),
                amount = "10000"
            )

            val paymentExecutor = TossPaymentExecutor(
                tossPaymentWebClient = pspTestWebClientConfiguration.createTestTossWebClient(
                    Pair("TossPayments-Test-Code", errorScenario.errorCode)
                ),
                uri = "/v1/payments/key-in"
            )

            try {
                paymentExecutor.execute(command).block()
            } catch (e: PSPConfirmationException) {
                assertThat(e.isSuccess).isEqualTo(errorScenario.isSuccess)
                assertThat(e.isFailure).isEqualTo(errorScenario.isFailure)
                assertThat(e.isUnknown).isEqualTo(errorScenario.isUnknown)
            }
        }
    }

    private fun generateErrorScenarios(): List<ErrorScenario> {
        return TossPaymentError.entries.map { error ->
            ErrorScenario(
                errorCode = error.name,
                isSuccess = error.isSuccess(),
                isFailure = error.isFailure(),
                isUnknown = error.isUnknown()
            )
        }
    }
}

private data class ErrorScenario(
    val errorCode: String,
    val isFailure: Boolean,
    val isUnknown: Boolean,
    val isSuccess: Boolean
)