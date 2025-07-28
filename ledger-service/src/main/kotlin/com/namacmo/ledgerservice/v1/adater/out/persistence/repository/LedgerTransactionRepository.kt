package com.namacmo.ledgerservice.v1.adater.out.persistence.repository

import com.namacmo.ledgerservice.v1.domain.PaymentEventMessage


interface LedgerTransactionRepository {

  fun isExist(message: PaymentEventMessage): Boolean
}