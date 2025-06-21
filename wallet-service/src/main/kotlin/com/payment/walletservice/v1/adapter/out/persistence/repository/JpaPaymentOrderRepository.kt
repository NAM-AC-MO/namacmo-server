package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.adapter.out.persistence.entity.JpaPaymentOrderMapper
import com.payment.walletservice.v1.domain.model.PaymentOrder
import org.springframework.stereotype.Repository

@Repository
class JpaPaymentOrderRepository (
  private val springDataJpaPaymentOrderRepository: SpringDataJpaPaymentOrderRepository,
  private val jpaPaymentOrderMapper: JpaPaymentOrderMapper
) : PaymentOrderRepository {

  override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
    return springDataJpaPaymentOrderRepository.findByOrderId(orderId)
      .map { jpaPaymentOrderMapper.mapToDomainEntity(it) }
  }
}