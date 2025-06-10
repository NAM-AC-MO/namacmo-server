package com.namacmo.paymentservice.v1.application.service

import com.namacmo.paymentservice.test.PaymentDatabaseHelper
import com.namacmo.paymentservice.test.PaymentTestConfiguration
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutCommand
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutUseCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@Import(PaymentTestConfiguration::class)
class CheckoutServiceTest (
    @Autowired private val checkoutUseCase: CheckoutUseCase,
    @Autowired private val paymentDatabaseHelper: PaymentDatabaseHelper
) {

    @AfterEach
    fun tearDown() {
        paymentDatabaseHelper.clean().block()
    }

    @Test
    fun `should save PaymentEvent and PaymentOrder successfully`() {
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        StepVerifier.create(checkoutUseCase.checkout(checkoutCommand))
            .expectNextMatches {
                it.amount.value.toInt() == 60000 && it.orderId == orderId
            }
            .verifyComplete()

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!
        val paymentOrders = paymentEvent.getPaymentOrders()

        assertThat(paymentEvent.orderId).isEqualTo(orderId)
        assertThat(paymentEvent.totalAmount().value).isEqualByComparingTo(BigDecimal("60000"))
        assertThat(paymentOrders.size).isEqualTo(checkoutCommand.productIds.size)
        assertFalse(paymentEvent.isPaymentDone())
        assertTrue(paymentOrders.all { !it.isLedgerUpdated() })
        assertTrue(paymentOrders.all { !it.isWalletUpdated() })
    }

    @Test
    fun `should fail to save PaymentEvent and PaymentOrder when trying to save for the second time`() {
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
            cartId = "1",
            buyerId = "1",
            productIds = listOf("1", "2", "3"),
            idempotencyKey = orderId
        )

        checkoutUseCase.checkout(checkoutCommand).block()

        org.junit.jupiter.api.assertThrows<DataIntegrityViolationException> {
            checkoutUseCase.checkout(checkoutCommand).block()
        }
    }
}