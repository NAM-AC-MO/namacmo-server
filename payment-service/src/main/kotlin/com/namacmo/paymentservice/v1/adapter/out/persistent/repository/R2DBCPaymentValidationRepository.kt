package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.paymentservice.v1.adapter.out.persistent.exception.PaymentValidationException
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Repository
class R2DBCPaymentValidationRepository (
  private val databaseClient: DatabaseClient
) : PaymentValidationRepository {

  private val log = LoggerFactory.getLogger(R2DBCPaymentValidationRepository::class.java)

  override fun isValid(orderId: String, amount: String): Mono<Boolean> {
    return databaseClient.sql(SELECT_PAYMENT_TOTAL_AMOUNT_QUERY)
      .bind("orderId", orderId)
      .fetch()
      .first()
      .handle { row, sink ->
        if ((row["total_amount"] as BigDecimal).compareTo(BigDecimal(amount)) == 0) {
          sink.next(true)
        } else {
          log.error("row={} parameter amount={}", row["total_amount"] as BigDecimal, BigDecimal(amount))
          sink.error(PaymentValidationException("결제 (orderId: $orderId) 에서 금액 (amount: $amount)이 올바르지 않습니다."))
        }
      }
  }

  companion object {
    val SELECT_PAYMENT_TOTAL_AMOUNT_QUERY = """
      SELECT SUM(amount) as total_amount
      FROM payment_orders
      WHERE order_id = :orderId
    """.trimIndent()
  }
}