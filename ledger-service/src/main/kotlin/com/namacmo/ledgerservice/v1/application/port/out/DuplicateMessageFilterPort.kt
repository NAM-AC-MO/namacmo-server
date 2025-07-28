package com.namacmo.ledgerservice.v1.application.port.out

import com.namacmo.ledgerservice.v1.domain.PaymentEventMessage

interface DuplicateMessageFilterPort {

  fun isAlreadyProcess(message: PaymentEventMessage): Boolean
}