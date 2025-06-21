package com.payment.walletservice.v1.application.port.out

import com.payment.walletservice.v1.domain.PaymentEventMessage

interface DuplicateMessageFilterPort {

  fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean
}