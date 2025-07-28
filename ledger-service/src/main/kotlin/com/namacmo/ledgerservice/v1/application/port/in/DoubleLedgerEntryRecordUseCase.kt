package com.namacmo.ledgerservice.v1.application.port.`in`

import com.namacmo.ledgerservice.v1.domain.LedgerEventMessage
import com.namacmo.ledgerservice.v1.domain.PaymentEventMessage

interface DoubleLedgerEntryRecordUseCase {

  fun recordDoubleLedgerEntry(message: PaymentEventMessage): LedgerEventMessage
}