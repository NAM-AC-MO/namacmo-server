package com.payment.walletservice.v1.adapter.out.persistence.entity

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.adapter.out.persistence.converter.MoneyToBigDecimalConvert
import com.payment.walletservice.v1.domain.valueobject.TransactionType
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "wallet_transactions")
class JpaWalletTransactionEntity (

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "wallet_id")
  val walletId: Long,

  @Column
  @Convert(converter = MoneyToBigDecimalConvert::class)
  val amount: Money,

  @Enumerated(value = EnumType.STRING)
  val type: TransactionType,

  @Column(name = "order_id")
  val orderId: String,

  @Column(name = "reference_type")
  val referenceType: String,

  @Column(name = "reference_id")
  val referenceId: String,

  @Column(name = "idempotency_key")
  val idempotencyKey: String
)