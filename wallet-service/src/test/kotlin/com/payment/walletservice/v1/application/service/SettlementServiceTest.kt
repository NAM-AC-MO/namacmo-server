package com.payment.walletservice.v1.application.service

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.adapter.out.persistence.entity.JpaWalletEntity
import com.payment.walletservice.v1.adapter.out.persistence.repository.SaveWalletPort
import com.payment.walletservice.v1.adapter.out.persistence.repository.SpringDataJpaWalletRepository
import com.payment.walletservice.v1.adapter.out.persistence.repository.SpringDataJpaWalletTransactionRepository
import com.payment.walletservice.v1.application.port.out.DuplicateMessageFilterPort
import com.payment.walletservice.v1.application.port.out.LoadPaymentOrderPort
import com.payment.walletservice.v1.application.port.out.LoadWalletPort
import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.PaymentEventMessageType
import com.payment.walletservice.v1.domain.WalletEventMessageType
import com.payment.walletservice.v1.domain.model.PaymentOrder
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class SettlementServiceTest(
    @Autowired private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
    @Autowired private val loadWalletPort: LoadWalletPort,
    @Autowired private val saveWalletPort: SaveWalletPort,
    @Autowired private val springDataJpaWalletRepository: SpringDataJpaWalletRepository,
    @Autowired private val springDataJpaWalletTransactionRepository: SpringDataJpaWalletTransactionRepository
) {
    @AfterEach
    fun tearDown() {
        springDataJpaWalletTransactionRepository.deleteAllInBatch()
        springDataJpaWalletRepository.deleteAllInBatch()
    }

    @Test
    fun `should process settlement successfully`() {
        val jpaWalletEntities = listOf(
            JpaWalletEntity(
                userId = 1L,
                balance = Money.ZERO,
                version = 0,
            ),
            JpaWalletEntity(
                userId = 2L,
                balance = Money.ZERO,
                version = 0,
            )
        )

        springDataJpaWalletRepository.saveAll(jpaWalletEntities)

        val paymentEventMessage = PaymentEventMessage(
            type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
            payload = mapOf(
                "orderId" to UUID.randomUUID().toString()
            )
        )

        val mockLoadPaymentOrderPort = mockk<LoadPaymentOrderPort>()

        every { mockLoadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId()) } returns listOf(
            PaymentOrder(
                id = 1,
                sellerId = 1,
                amount = Money("3000"),
                orderId = paymentEventMessage.orderId()
            ),
            PaymentOrder(
                id = 2,
                sellerId = 2,
                amount = Money("4000"),
                orderId = paymentEventMessage.orderId()
            )
        )

        val settlementService = SettlementService(
            duplicateMessageFilterPort = duplicateMessageFilterPort,
            loadWalletPort = loadWalletPort,
            loadPaymentOrderPort = mockLoadPaymentOrderPort,
            saveWalletPort = saveWalletPort
        )

        val walletEventMessage = settlementService.processSettlement(paymentEventMessage)

        val updatedWallets = loadWalletPort.getWallets(jpaWalletEntities.map { it.userId }.toSet())
            .sortedBy { it.userId }

        assertThat(walletEventMessage.payload["orderId"]).isEqualTo(paymentEventMessage.orderId())
        assertThat(walletEventMessage.type).isEqualTo(WalletEventMessageType.SUCCESS)
        assertThat(updatedWallets[0].balance.isEqualTo(Money("3000"))).isTrue()
        assertThat(updatedWallets[1].balance.isEqualTo(Money("4000"))).isTrue()
    }

    @Test
    fun `should be processed only once even if it is called multiple times`()  {
        val jpaWalletEntities = listOf(
            JpaWalletEntity(
                userId = 1L,
                balance = Money.ZERO,
                version = 0,
            ),
            JpaWalletEntity(
                userId = 2L,
                balance = Money.ZERO,
                version = 0,
            )
        )

        springDataJpaWalletRepository.saveAll(jpaWalletEntities)

        val paymentEventMessage = PaymentEventMessage(
            type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
            payload = mapOf(
                "orderId" to UUID.randomUUID().toString()
            )
        )

        val mockLoadPaymentOrderPort = mockk<LoadPaymentOrderPort>()

        every { mockLoadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId()) } returns listOf(
            PaymentOrder(
                id = 1,
                sellerId = 1,
                amount = Money("3000"),
                orderId = paymentEventMessage.orderId()
            ),
            PaymentOrder(
                id = 2,
                sellerId = 2,
                amount = Money("4000"),
                orderId = paymentEventMessage.orderId()
            )
        )

        val settlementService = SettlementService(
            duplicateMessageFilterPort = duplicateMessageFilterPort,
            loadWalletPort = loadWalletPort,
            loadPaymentOrderPort = mockLoadPaymentOrderPort,
            saveWalletPort = saveWalletPort
        )

        settlementService.processSettlement(paymentEventMessage)
        settlementService.processSettlement(paymentEventMessage)

        val updatedWallets = loadWalletPort.getWallets(jpaWalletEntities.map { it.userId }.toSet())
            .sortedBy { it.userId }

        assertThat(updatedWallets[0].balance.isEqualTo(Money("3000"))).isTrue()
        assertThat(updatedWallets[1].balance.isEqualTo(Money("4000"))).isTrue()
    }

}