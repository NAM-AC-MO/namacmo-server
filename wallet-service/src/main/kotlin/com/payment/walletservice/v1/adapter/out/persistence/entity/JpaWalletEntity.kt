package com.payment.walletservice.v1.adapter.out.persistence.entity

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.adapter.out.persistence.converter.MoneyToBigDecimalConvert
import jakarta.persistence.*

@Entity
@Table(name = "wallets")
data class JpaWalletEntity (

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "user_id")
  val userId: Long,

  @Convert(converter = MoneyToBigDecimalConvert::class)
  val balance: Money,

  @Version
  val version: Int
) {

  fun addBalance(amount: Money): JpaWalletEntity {
    return copy(balance = balance.add(amount))
  }
}