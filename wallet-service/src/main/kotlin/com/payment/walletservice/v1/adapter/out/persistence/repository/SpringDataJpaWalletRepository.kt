package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.adapter.out.persistence.entity.JpaWalletEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataJpaWalletRepository : JpaRepository<JpaWalletEntity, Long> {

    fun findByUserIdIn(userIds: Set<Long>): List<JpaWalletEntity>

    fun findByIdIn(ids: Set<Long>): List<JpaWalletEntity>
}