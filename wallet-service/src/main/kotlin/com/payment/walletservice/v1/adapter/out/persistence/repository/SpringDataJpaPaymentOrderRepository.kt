package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.adapter.out.persistence.entity.JpaPaymentOrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataJpaPaymentOrderRepository : JpaRepository<JpaPaymentOrderEntity, Long> {

    fun findByOrderId(orderId: String): List<JpaPaymentOrderEntity>
}