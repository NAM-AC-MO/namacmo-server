package com.payment.walletservice.v1.adapter.out.persistence.entity

import com.payment.walletservice.v1.domain.model.PaymentOrder
import org.springframework.stereotype.Component

@Component
class JpaPaymentOrderMapper {

  fun mapToDomainEntity(jpaPaymentOrderEntity: JpaPaymentOrderEntity): PaymentOrder {
    return PaymentOrder(
      id = jpaPaymentOrderEntity.id!!,
      sellerId = jpaPaymentOrderEntity.sellerId,
      amount = jpaPaymentOrderEntity.amount,
      orderId = jpaPaymentOrderEntity.orderId
    )
  }
}