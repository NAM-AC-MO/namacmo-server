package com.namacmo.paymentservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutCommand
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutUseCase
import com.namacmo.paymentservice.v1.application.port.out.LoadProductPort
import com.namacmo.paymentservice.v1.application.port.out.SavePaymentPort
import com.namacmo.paymentservice.v1.domain.CheckoutResult
import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import com.namacmo.paymentservice.v1.domain.entity.PaymentOrder
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import com.namacmo.paymentservice.v1.domain.entity.Product
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus
import reactor.core.publisher.Mono
import java.util.UUID

@UseCase
class CheckoutService (
  private val loadProductPort: LoadProductPort,
  private val savePaymentPort: SavePaymentPort
) : CheckoutUseCase {

  override fun checkout(command: CheckoutCommand): Mono<CheckoutResult> {
    return loadProductPort.getProducts(command.cartId, command.productIds)
      .collectList()
      .map { createPaymentEvent(command, it) }
      .flatMap { savePaymentPort.save(it).thenReturn(it)  }
      .map { CheckoutResult(amount = it.totalAmount(), orderId = it.orderId, orderName = it.orderName) }
  }

  private fun createPaymentEvent(command: CheckoutCommand, products: List<Product>): PaymentEvent {
    val paymentEventId = UUID.randomUUID().toString()

    return PaymentEvent(
      id = paymentEventId,
      buyerId = command.buyerId,
      orderId = command.idempotencyKey,
      orderName = products.joinToString { it.name },
      paymentOrders = PaymentOrders(
        products.map {
          PaymentOrder(
            id = UUID.randomUUID().toString(),
            paymentEventId = paymentEventId,
            sellerId = it.sellerId,
            orderId = command.idempotencyKey, // orderId로 멱등적 키를 생성
            productId = it.id,
            amount = it.amount,
            paymentStatus = PaymentStatus.NOT_STARTED, // 초기값으로 NOT_STARTED 셋팅
          )
        }
      )
    )
  }
}