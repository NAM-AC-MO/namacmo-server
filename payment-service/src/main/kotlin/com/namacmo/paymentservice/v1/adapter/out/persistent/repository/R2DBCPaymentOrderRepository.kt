package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.entity.PaymentOrder
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Repository
class R2DBCPaymentOrderRepository(
    private val databaseClient: DatabaseClient
): PaymentOrderRepository {

    override fun findByOrderId(orderId: String): Mono<PaymentOrders> {
        return databaseClient.sql(SELECT_PAYMENT_ORDER_QUERY)
            .bind("orderId", orderId)
            .fetch()
            .all()
            .map {
                PaymentOrder(
                    id = it["id"] as String,
                    paymentEventId = it["payment_event_id"] as String,
                    sellerId = it["seller_id"] as String,
                    productId = it["product_id"] as String,
                    orderId = it["order_id"] as String,
                    amount = Money(it["amount"] as BigDecimal),
                    paymentStatus = PaymentStatus.findByName(it["payment_order_status"] as String),
                    isLedgerUpdated = (it["ledger_updated"] as Byte).toInt() != 0,
                    isWalletUpdated = (it["wallet_updated"] as Byte).toInt() != 0
                )
            }
            .collectList()
            .map(::PaymentOrders)
    }

    companion object {
        val SELECT_PAYMENT_ORDER_QUERY = """
            SELECT po.id, po.payment_event_id, po.seller_id, po.product_id,
                   po.order_id, po.amount, po.payment_order_status, po.ledger_updated, po.wallet_updated
            FROM payment_orders po WHERE po.order_id = :orderId
        """.trimIndent()
    }
}