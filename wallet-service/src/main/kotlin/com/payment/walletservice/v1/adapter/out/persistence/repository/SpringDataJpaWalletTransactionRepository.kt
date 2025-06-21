package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.adapter.out.persistence.entity.JpaWalletTransactionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataJpaWalletTransactionRepository : JpaRepository<JpaWalletTransactionEntity, Long> {

    fun existsByOrderId(orderId: String): Boolean
}