package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.adapter.out.persistence.entity.JpaWalletTransactionMapper
import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.model.WalletTransaction
import org.springframework.stereotype.Repository

@Repository
class JpaWalletTransactionRepository (
  private val springDataJpaWalletTransactionRepository: SpringDataJpaWalletTransactionRepository,
  private val jpaWalletTransactionMapper: JpaWalletTransactionMapper
) : WalletTransactionRepository {

  override fun isExist(paymentEventMessage: PaymentEventMessage): Boolean {
    return springDataJpaWalletTransactionRepository.existsByOrderId(paymentEventMessage.orderId())
  }

  override fun save(walletTransactions: List<WalletTransaction>) {
    springDataJpaWalletTransactionRepository.saveAll(walletTransactions.map { jpaWalletTransactionMapper.mapToJpaEntity(it) })
  }
}
