package com.namacmo.ledgerservice.v1.adater.out.persistence.repository

import com.namacmo.ledgerservice.v1.adater.out.persistence.entity.JpaPaymentOrderEntity
import com.namacmo.ledgerservice.v1.adater.out.persistence.entity.JpaPaymentOrderMapper
import com.namacmo.ledgerservice.v1.domain.PaymentOrder
import org.springframework.data.jpa.repository.JpaRepository
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

interface SpringDataJpaPaymentOrderRepository : JpaRepository<JpaPaymentOrderEntity, Long> {

  fun findByOrderId(orderId: String): List<JpaPaymentOrderEntity>
}