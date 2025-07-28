package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.appcommon.utils.DBDateFormat
import com.namacmo.paymentservice.v1.domain.PendingPaymentEvent
import com.namacmo.paymentservice.v1.domain.PendingPaymentOrder
import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import com.namacmo.paymentservice.v1.domain.entity.PaymentOrder
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus
import com.namacmo.paymentservice.v1.domain.valueobject.PendingPaymentOrders
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.text.get

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

    override fun getPayment(orderId: String): Mono<PaymentEvent> {
        return databaseClient.sql(SELECT_PAYMENT_EVENT_QUERY)
            .bind("orderId", orderId)
            .fetch()
            .all()
            .groupBy { it["payment_event_id"] as String }
            .flatMap { groupedFlux ->
                groupedFlux.collectList().map { results ->
                    val paymentOrders = results.map { result ->
                        PaymentOrder(
                            id = result["payment_order_id"] as String,
                            paymentEventId = groupedFlux.key(),
                            sellerId = result["seller_id"] as String,
                            productId = result["product_id"] as String,
                            orderId = result["order_id"] as String,
                            amount = Money(result["amount"] as BigDecimal),
                            paymentStatus = PaymentStatus.findByName(result["payment_order_status"] as String),
                            isLedgerUpdated = if (((result["ledger_updated"] as Byte).toInt()) == 1) true else false,
                            isWalletUpdated = if (((result["wallet_updated"] as Byte).toInt()) == 1) true else false
                        )
                    }

                    PaymentEvent(
                        id = groupedFlux.key(),
                        orderId = results.first()["order_id"] as String,
                        buyerId = results.first()["buyer_id"] as String,
                        orderName = results.first()["order_name"] as String,
                        paymentOrders = PaymentOrders(paymentOrders),
                        isPaymentDone = if (((results.first()["is_payment_done"] as Byte).toInt()) == 1) true else false
                    )
                }
            }
            .toMono()
    }

    override fun complete(paymentEvent: PaymentEvent): Mono<Void> {
        return when {
            paymentEvent.isPaymentDone() -> handlePaymentCompletion(paymentEvent)
            paymentEvent.isLedgerUpdateDone() -> handleLedgerUpdate(paymentEvent)
            paymentEvent.isWalletUpdateDone() -> handleWalletUpdate(paymentEvent)
            else -> error("Incorrect state for PaymentEvent id: ${paymentEvent.id}")
        }
    }

    private fun handlePaymentCompletion(paymentEvent: PaymentEvent): Mono<Void> {
        // 어떤것이 업데이트 되었는지 알 수 없기 때문에 둘다 한번에 업데이트 하기 위해 Mono.when 사용
        return Mono.`when`(
            handleLedgerUpdate(paymentEvent),
            handleWalletUpdate(paymentEvent)
        ).then(Mono.defer { completePaymentEvent(paymentEvent) })
    }

    private fun handleLedgerUpdate(paymentEvent: PaymentEvent): Mono<Void> {
        return databaseClient.sql(UPDATE_PAYMENT_ORDER_LEDGER_DONE_QUERY)
            .bind("paymentEventId", paymentEvent.id!!)
            .fetch()
            .rowsUpdated()
            .then()
    }

    private fun handleWalletUpdate(paymentEvent: PaymentEvent): Mono<Void> {
        return databaseClient.sql(UPDATE_PAYMENT_ORDER_WALLET_DONE_QUERY)
            .bind("paymentEventId", paymentEvent.id!!)
            .fetch()
            .rowsUpdated()
            .then()
    }

    private fun completePaymentEvent(paymentEvent: PaymentEvent): Mono<Void> {
        return databaseClient.sql(UPDATE_PAYMENT_EVENT_COMPLETE_QUERY)
            .bind("paymentEventId", paymentEvent.id!!)
            .fetch()
            .rowsUpdated()
            .then()
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

        val SELECT_PAYMENT_EVENT_QUERY = """
          SELECT pe.id as payment_event_id, po.id as payment_order_id, pe.order_id, pe.order_name, pe.buyer_id, pe.is_payment_done, po.seller_id, po.product_id, po.payment_order_status, po.amount, po.ledger_updated, po.wallet_updated 
          FROM payment_events pe
          INNER JOIN payment_orders po ON pe.order_id = po.order_id
          WHERE pe.order_id = :orderId 
        """.trimIndent()

        val UPDATE_PAYMENT_ORDER_LEDGER_DONE_QUERY = """ 
          UPDATE payment_orders 
          SET ledger_updated = true  
          WHERE payment_event_id = :paymentEventId
        """.trimIndent()

        val UPDATE_PAYMENT_ORDER_WALLET_DONE_QUERY = """
          UPDATE payment_orders 
          SET wallet_updated = true  
          WHERE payment_event_id = :paymentEventId
        """.trimIndent()

        val UPDATE_PAYMENT_EVENT_COMPLETE_QUERY = """
          UPDATE payment_events 
          SET is_payment_done = true
          WHERE id = :paymentEventId
        """.trimIndent()
    }

}