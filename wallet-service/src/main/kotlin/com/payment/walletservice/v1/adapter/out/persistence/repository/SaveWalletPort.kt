package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.domain.model.Wallet

interface SaveWalletPort {

  fun save(wallets: List<Wallet>)
}