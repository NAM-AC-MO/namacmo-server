package com.payment.walletservice.v1.application.port.out

import com.payment.walletservice.v1.domain.model.Wallet

interface LoadWalletPort {

  fun getWallets(sellerIds: Set<Long>): Set<Wallet>
}