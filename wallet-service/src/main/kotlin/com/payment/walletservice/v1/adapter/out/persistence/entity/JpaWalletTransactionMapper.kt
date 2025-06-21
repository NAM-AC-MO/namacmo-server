package com.payment.walletservice.v1.adapter.out.persistence.entity

import com.namacmo.appcommon.utils.IdempotencyCreator
import com.payment.walletservice.v1.domain.model.WalletTransaction
import org.springframework.stereotype.Component

@Component
class JpaWalletTransactionMapper {

  fun mapToJpaEntity(walletTransaction: WalletTransaction): JpaWalletTransactionEntity {
    return JpaWalletTransactionEntity(
      walletId = walletTransaction.walletId,
      amount = walletTransaction.amount,
      type = walletTransaction.type,
      referenceType = walletTransaction.referenceType.name,
      referenceId = walletTransaction.referenceId,
      orderId = walletTransaction.orderId,
      idempotencyKey = IdempotencyCreator.create(walletTransaction)
    )
  }
}