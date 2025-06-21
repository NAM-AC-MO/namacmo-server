package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.model.WalletTransaction

interface WalletTransactionRepository {

  fun isExist(paymentEventMessage: PaymentEventMessage): Boolean

  fun save(walletTransactions: List<WalletTransaction>)
}