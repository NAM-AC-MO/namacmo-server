package com.namacmo.ledgerservice.v1.adater.out.persistence.entity

import com.namacmo.appcommon.utils.IdempotencyCreator
import com.namacmo.ledgerservice.v1.domain.LedgerTransaction
import org.springframework.stereotype.Component


@Component
class JpaLedgerTransactionMapper {

  fun mapToJpaEntity(ledgerTransaction: LedgerTransaction): JpaLedgerTransactionEntity {
    return JpaLedgerTransactionEntity(
      description = "LedgerService record transaction",
      referenceType = ledgerTransaction.referenceType.name,
      referenceId = ledgerTransaction.referenceId,
      orderId = ledgerTransaction.orderId,
      idempotencyKey = IdempotencyCreator.create(ledgerTransaction)
    )
  }
}