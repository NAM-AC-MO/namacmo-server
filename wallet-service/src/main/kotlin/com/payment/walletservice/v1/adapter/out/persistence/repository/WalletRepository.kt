package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.domain.model.Wallet

interface WalletRepository {

  fun getWallets(sellerIds: Set<Long>): Set<Wallet>

  fun save(wallets: List<Wallet>)
}