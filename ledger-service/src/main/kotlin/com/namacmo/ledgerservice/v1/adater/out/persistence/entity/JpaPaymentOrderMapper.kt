package com.namacmo.ledgerservice.v1.adater.out.persistence.entity

import com.namacmo.ledgerservice.v1.domain.PaymentOrder
import org.springframework.stereotype.Component

@Component
class JpaPaymentOrderMapper {

  fun mapToDomainEntity(jpaPaymentOrderEntity: JpaPaymentOrderEntity): PaymentOrder {
    return PaymentOrder(
      id = jpaPaymentOrderEntity.id!!,
      amount = jpaPaymentOrderEntity.amount.toLong(),
      orderId = jpaPaymentOrderEntity.orderId
    )
  }
}