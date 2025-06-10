package com.namacmo.paymentservice.v1.application.service

import com.namacmo.paymentservice.test.PaymentDatabaseHelper
import com.namacmo.paymentservice.test.PaymentTestConfiguration
import com.namacmo.paymentservice.v1.adapter.out.persistent.exception.PaymentValidationException
import com.namacmo.paymentservice.v1.adapter.out.web.toss.exception.PSPConfirmationException
import com.namacmo.paymentservice.v1.adapter.out.web.toss.exception.TossPaymentError
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutCommand
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutUseCase
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmCommand
import com.namacmo.paymentservice.v1.application.port.out.PaymentExecutorPort
import com.namacmo.paymentservice.v1.application.port.out.PaymentStatusUpdatePort
import com.namacmo.paymentservice.v1.application.port.out.PaymentValidationPort
import com.namacmo.paymentservice.v1.domain.PaymentExecutionResult
import com.namacmo.paymentservice.v1.domain.PaymentExtraDetails
import com.namacmo.paymentservice.v1.domain.PaymentFailure
import com.namacmo.paymentservice.v1.domain.valueobject.PSPConfirmationStatus
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentMethodGroup
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentType
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@SpringBootTest
@Import(PaymentTestConfiguration::class)
class PaymentConfirmServiceTest(
    @Autowired private val checkoutUseCase: CheckoutUseCase,
    @Autowired private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
    @Autowired private val paymentValidationPort: PaymentValidationPort,
    @Autowired private val paymentDatabaseHelper: PaymentDatabaseHelper,
    @Autowired private val paymentErrorHandler: PaymentErrorHandler
) {
    private val mockPaymentExecutorPort = mockk<PaymentExecutorPort>()

    @AfterEach
    fun tearDown() {
        paymentDatabaseHelper.clean().block()
    }

    @Test
    fun `should be marked as SUCCESS if Payment Confirmation success in PSP`() {
        Hooks.onOperatorDebug()

        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount.toStringValue()
        )

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
            paymentErrorHandler = paymentErrorHandler
        )

        val paymentExecutionResult = PaymentExecutionResult(
            paymentKey = paymentConfirmCommand.paymentKey,
            orderId = paymentConfirmCommand.orderId,
            extraDetails = PaymentExtraDetails(
                type = PaymentType.TOSS,
                methodGroup = PaymentMethodGroup.EASY_PAY,
                totalAmount = paymentConfirmCommand.amount,
                orderName = "test_order_name",
                pspConfirmationStatus = PSPConfirmationStatus.DONE,
                approvedAt = LocalDateTime.now(),
                pspRawData = "{}"
            ),
            isSuccess = true,
            isRetryable = false,
            isUnknown = false,
            isFailure = false
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.just(paymentExecutionResult)

        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.SUCCESS)
        assertTrue(paymentEvent.isSuccess())
        assertThat(paymentEvent.paymentType).isEqualTo(paymentExecutionResult.extraDetails!!.type)
        assertThat(paymentEvent.paymentMethodGroup).isEqualTo(paymentExecutionResult.extraDetails!!.methodGroup)
        assertThat(paymentEvent.orderName).isEqualTo(paymentExecutionResult.extraDetails!!.orderName)
        assertThat(paymentEvent.approvedAt?.truncatedTo(ChronoUnit.MINUTES)).isEqualTo(
            paymentExecutionResult.extraDetails!!.approvedAt.truncatedTo(
                ChronoUnit.MINUTES
            )
        )
    }

    @Test
    fun `should be marked as FAILURE if Payment Confirmation fails on PSP`() {
        Hooks.onOperatorDebug()

        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount.toStringValue()
        )

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
            paymentErrorHandler = paymentErrorHandler
        )

        val paymentExecutionResult = PaymentExecutionResult(
            paymentKey = paymentConfirmCommand.paymentKey,
            orderId = paymentConfirmCommand.orderId,
            extraDetails = PaymentExtraDetails(
                type = PaymentType.TOSS,
                methodGroup = PaymentMethodGroup.EASY_PAY,
                totalAmount = paymentConfirmCommand.amount,
                orderName = "test_order_name",
                pspConfirmationStatus = PSPConfirmationStatus.DONE,
                approvedAt = LocalDateTime.now(),
                pspRawData = "{}"
            ),
            failure = PaymentFailure("ERROR", "Test Error"),
            isSuccess = false,
            isRetryable = false,
            isUnknown = false,
            isFailure = true
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.just(paymentExecutionResult)

        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.FAILURE)
        assertTrue(paymentEvent.isFailure())
    }

    @Test
    fun `should be marked as UNKNOWN if Payment Confirmation fails due to an unknown exception`() {
        Hooks.onOperatorDebug()

        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount.toStringValue()
        )

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
            paymentErrorHandler = paymentErrorHandler
        )

        val paymentExecutionResult = PaymentExecutionResult(
            paymentKey = paymentConfirmCommand.paymentKey,
            orderId = paymentConfirmCommand.orderId,
            extraDetails = PaymentExtraDetails(
                type = PaymentType.TOSS,
                methodGroup = PaymentMethodGroup.EASY_PAY,
                totalAmount = paymentConfirmCommand.amount,
                orderName = "test_order_name",
                pspConfirmationStatus = PSPConfirmationStatus.DONE,
                approvedAt = LocalDateTime.now(),
                pspRawData = "{}"
            ),
            failure = PaymentFailure("ERROR", "Test Error"),
            isSuccess = false,
            isRetryable = false,
            isUnknown = true,
            isFailure = false
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.just(paymentExecutionResult)

        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.UNKNOWN)
        assertTrue(paymentEvent.isUnknown())
    }

    @Test
    fun `should handle PSPConfirmationException`() {
        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount.toStringValue()
        )

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
            paymentErrorHandler = paymentErrorHandler
        )

        val pspConfirmationException = PSPConfirmationException(
            errorCode = TossPaymentError.REJECT_ACCOUNT_PAYMENT.name,
            errorMessage = TossPaymentError.REJECT_ACCOUNT_PAYMENT.description,
            isSuccess = false,
            isFailure = true,
            isUnknown = false,
            isRetryableError = false
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.error(pspConfirmationException)

        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!
        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.FAILURE)
        assertTrue(paymentEvent.isFailure())
    }

    @Test
    fun `should handle PaymentValidationException`() {
        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount.toStringValue()
        )

        val mockPaymentValidationPort = mockk<PaymentValidationPort>()

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentExecutorPort = mockPaymentExecutorPort,
            paymentValidationPort = mockPaymentValidationPort,
            paymentErrorHandler = paymentErrorHandler
        )

        val paymentValidationException = PaymentValidationException("결제 유효성 검증에서 실패하였습니다.")

        every { mockPaymentValidationPort.isValid(orderId, paymentConfirmCommand.amount) } returns Mono.error(
            paymentValidationException
        )

        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!
        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.FAILURE)
        assertTrue(paymentEvent.isFailure())
    }

    @Test
    fun `should handle PaymentAlreadyProcessedException`() {
        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount.toStringValue()
        )


        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
            paymentErrorHandler = paymentErrorHandler
        )

        val paymentExecutionResult = PaymentExecutionResult(
            paymentKey = paymentConfirmCommand.paymentKey,
            orderId = paymentConfirmCommand.orderId,
            extraDetails = PaymentExtraDetails(
                type = PaymentType.TOSS,
                methodGroup = PaymentMethodGroup.EASY_PAY,
                totalAmount = paymentConfirmCommand.amount,
                orderName = "test_order_name",
                pspConfirmationStatus = PSPConfirmationStatus.DONE,
                approvedAt = LocalDateTime.now(),
                pspRawData = "{}"
            ),
            isSuccess = true,
            isRetryable = false,
            isUnknown = false,
            isFailure = false
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.just(paymentExecutionResult)

        paymentConfirmService.confirm(paymentConfirmCommand).block()!!
        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.SUCCESS)
        assertTrue(paymentEvent.isSuccess())
    }

    @Test
    @Tag("ExternalIntegration")
    fun `should send the event message to the external message system after the payment confirmation has been successful`() {
        Hooks.onOperatorDebug()

        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount.toStringValue()
        )

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
            paymentErrorHandler = paymentErrorHandler
        )

        val paymentExecutionResult = PaymentExecutionResult(
            paymentKey = paymentConfirmCommand.paymentKey,
            orderId = paymentConfirmCommand.orderId,
            extraDetails = PaymentExtraDetails(
                type = PaymentType.TOSS,
                methodGroup = PaymentMethodGroup.EASY_PAY,
                totalAmount = paymentConfirmCommand.amount,
                orderName = "test_order_name",
                pspConfirmationStatus = PSPConfirmationStatus.DONE,
                approvedAt = LocalDateTime.now(),
                pspRawData = "{}"
            ),
            isSuccess = true,
            isRetryable = false,
            isUnknown = false,
            isFailure = false
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.just(paymentExecutionResult)

        paymentConfirmService.confirm(paymentConfirmCommand).block()!!

        Thread.sleep(10000)
    }
}