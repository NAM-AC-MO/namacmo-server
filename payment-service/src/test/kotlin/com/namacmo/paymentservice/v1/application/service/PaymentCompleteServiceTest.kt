package com.namacmo.paymentservice.v1.application.service

import com.namacmo.paymentservice.test.PaymentDatabaseHelper
import com.namacmo.paymentservice.test.PaymentTestConfiguration
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutCommand
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutUseCase
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentCompleteUseCase
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmCommand
import com.namacmo.paymentservice.v1.application.port.out.PaymentExecutorPort
import com.namacmo.paymentservice.v1.application.port.out.PaymentStatusUpdatePort
import com.namacmo.paymentservice.v1.application.port.out.PaymentValidationPort
import com.namacmo.paymentservice.v1.domain.LedgerEventMessage
import com.namacmo.paymentservice.v1.domain.LedgerEventMessageType
import com.namacmo.paymentservice.v1.domain.PaymentExecutionResult
import com.namacmo.paymentservice.v1.domain.PaymentExtraDetails
import com.namacmo.paymentservice.v1.domain.WalletEventMessage
import com.namacmo.paymentservice.v1.domain.WalletEventMessageType
import com.namacmo.paymentservice.v1.domain.valueobject.PSPConfirmationStatus
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentMethodGroup
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
@Import(PaymentTestConfiguration::class)
class PaymentCompleteServiceTest (
    @Autowired private val paymentDatabaseHelper: PaymentDatabaseHelper,
    @Autowired private val checkoutUseCase: CheckoutUseCase,
    @Autowired private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
    @Autowired private val paymentValidationPort: PaymentValidationPort,
    @Autowired private val paymentCompleteUseCase: PaymentCompleteUseCase,
    @Autowired private val paymentErrorHandler: PaymentErrorHandler
) {

    // PSP를 통해 결제 승인 요청을 하는 부분은 모킹 처리
    private val mockPaymentExecutorPort = mockk<PaymentExecutorPort>()

    @BeforeEach
    fun clean() {
        paymentDatabaseHelper.clean().block()
    }

    @Test
    fun `should update payment given a WalletEventMessage`() {
        Hooks.onOperatorDebug()

        val orderId = createPaymentEventWithSuccessStatus()

        val walletEventMessage = WalletEventMessage(
            type = WalletEventMessageType.SUCCESS,
            payload = mapOf(
                "orderId" to orderId
            )
        )

        paymentCompleteUseCase.completePayment(walletEventMessage).block()

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertTrue(paymentEvent.isWalletUpdateDone())
        assertFalse(paymentEvent.isLedgerUpdateDone())
        assertFalse(paymentEvent.isPaymentDone())
    }

    @Test
    fun `should update payment given a LedgerEventMessage`() {
        Hooks.onOperatorDebug()

        val orderId = createPaymentEventWithSuccessStatus()

        val ledgerEventMessage = LedgerEventMessage(
            type = LedgerEventMessageType.SUCCESS,
            payload = mapOf(
                "orderId" to orderId
            )
        )

        paymentCompleteUseCase.completePayment(ledgerEventMessage).block()

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertTrue(paymentEvent.isLedgerUpdateDone())
        assertFalse(paymentEvent.isWalletUpdateDone())
        assertFalse(paymentEvent.isPaymentDone())
    }

    @Test
    fun `should update payment given a LedgerEventMessage and WalletEventMessage`() {
        Hooks.onOperatorDebug()

        val orderId = createPaymentEventWithSuccessStatus()

        val ledgerEventMessage = LedgerEventMessage(
            type = LedgerEventMessageType.SUCCESS,
            payload = mapOf(
                "orderId" to orderId
            )
        )
        val walletEventMessage = WalletEventMessage(
            type = WalletEventMessageType.SUCCESS,
            payload = mapOf(
                "orderId" to orderId
            )
        )

        paymentCompleteUseCase.completePayment(ledgerEventMessage).block()
        paymentCompleteUseCase.completePayment(walletEventMessage).block()

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertTrue(paymentEvent.isPaymentDone())
        assertTrue(paymentEvent.isWalletUpdateDone())
        assertTrue(paymentEvent.isLedgerUpdateDone())
    }

    private fun createPaymentEventWithSuccessStatus(): String {
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

        return orderId
    }
}