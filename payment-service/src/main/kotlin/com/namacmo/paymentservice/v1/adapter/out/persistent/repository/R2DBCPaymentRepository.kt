package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.appcommon.utils.DBDateFormat
import com.namacmo.paymentservice.v1.domain.PendingPaymentEvent
import com.namacmo.paymentservice.v1.domain.PendingPaymentOrder
import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus
import com.namacmo.paymentservice.v1.domain.valueobject.PendingPaymentOrders
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
class R2DBCPaymentRepository(
    private val databaseClient: DatabaseClient,
    private val transactionalOperator: TransactionalOperator
) : PaymentRepository {

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return insertPaymentEvent(paymentEvent)
            .flatMap { bulkInsertPaymentOrders(paymentEvent) }
            .`as`(transactionalOperator::transactional)
            .then()
    }

    private fun insertPaymentEvent(paymentEvent: PaymentEvent): Mono<Long> {
        return databaseClient.sql(INSERT_PAYMENT_EVENT_QUERY)
            .bind("id", paymentEvent.id)
            .bind("buyerId", paymentEvent.buyerId)
            .bind("orderName", paymentEvent.orderName)
            .bind("orderId", paymentEvent.orderId)
            .fetch()
            .rowsUpdated()
    }

    private fun bulkInsertPaymentOrders(paymentEvent: PaymentEvent): Mono<Long> {
        val paymentOrders = paymentEvent.getPaymentOrders()
        val valueClauses = paymentOrders.joinToString(", ") { paymentOrder ->
            "('${paymentOrder.id}', '${paymentEvent.id}', '${paymentOrder.sellerId}', '${paymentOrder.orderId}', '${paymentOrder.productId}', ${paymentOrder.amount.value}, '${paymentOrder.paymentStatus}')"
        }

        return databaseClient.sql(BULK_INSERT_PAYMENT_ORDER_QUERY(valueClauses))
            .fetch()
            .rowsUpdated()
    }

    override fun getPendingPayments(): Flux<PendingPaymentEvent> {
        return databaseClient.sql(SELECT_PENDING_PAYMENT_QUERY)
            .bind("updatedAt", LocalDateTime.now().format(DBDateFormat.MYSQL.formatter))
            .fetch()
            .all()
            .groupBy { it["payment_event_id"] as String }
            .flatMap { groupedFlux ->
                groupedFlux.collectList().map { results ->
                    PendingPaymentEvent(
                        paymentEventId = groupedFlux.key(),
                        paymentKey = results.first()["payment_key"] as String,
                        orderId = results.first()["order_id"] as String,
                        pendingPaymentOrders = PendingPaymentOrders(
                            results.map {
                                PendingPaymentOrder(
                                    paymentOrderId = it["payment_order_id"] as String,
                                    status = PaymentStatus.findByName(it["payment_order_status"] as String),
                                    amount = Money((it["amount"] as BigDecimal)),
                                    failedCount = it["failed_count"] as Byte,
                                    threshold = it["threshold"] as Byte
                                )
                            }
                        )
                    )
                }
            }
    }

    companion object {
        val INSERT_PAYMENT_EVENT_QUERY = """
          INSERT INTO payment_events (id, buyer_id, order_name, order_id)
          VALUES (:id, :buyerId, :orderName, :orderId) 
        """.trimIndent()

        val BULK_INSERT_PAYMENT_ORDER_QUERY = fun(valueClauses: String) = """
          INSERT INTO payment_orders (id, payment_event_id, seller_id, order_id, product_id, amount, payment_order_status) 
          VALUES $valueClauses
        """.trimIndent()

        val SELECT_PENDING_PAYMENT_QUERY = """
          SELECT pe.id as payment_event_id, pe.payment_key, pe.order_id, po.id as payment_order_id, po.payment_order_status, po.amount, po.failed_count, po.threshold
          FROM payment_events pe
          INNER JOIN payment_orders po ON po.payment_event_id = pe.id
          WHERE (po.payment_order_status = 'UNKNOWN' OR (po.payment_order_status = 'EXECUTING' AND po.updated_at <= :updatedAt - INTERVAL 3 MINUTE))
          AND po.failed_count < po.threshold
          LIMIT 10 
        """.trimIndent()
    }

}