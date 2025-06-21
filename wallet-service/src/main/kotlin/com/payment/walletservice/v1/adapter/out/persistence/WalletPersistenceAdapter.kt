package com.payment.walletservice.v1.adapter.out.persistence

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.payment.walletservice.v1.adapter.out.persistence.repository.SaveWalletPort
import com.payment.walletservice.v1.adapter.out.persistence.repository.WalletRepository
import com.payment.walletservice.v1.adapter.out.persistence.repository.WalletTransactionRepository
import com.payment.walletservice.v1.application.port.out.DuplicateMessageFilterPort
import com.payment.walletservice.v1.application.port.out.LoadWalletPort
import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.model.Wallet

@PersistenceAdapter
class WalletPersistenceAdapter (
  private val walletTransactionRepository: WalletTransactionRepository,
  private val walletRepository: WalletRepository
) : DuplicateMessageFilterPort, LoadWalletPort, SaveWalletPort {

  override fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean {
    return walletTransactionRepository.isExist(paymentEventMessage)
  }

  override fun getWallets(sellerIds: Set<Long>): Set<Wallet> {
    return walletRepository.getWallets(sellerIds)
  }

  override fun save(wallets: List<Wallet>) {
    return walletRepository.save(wallets)
  }
}