package com.payment.walletservice.v1.adapter.out.persistence.entity

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.adapter.out.persistence.converter.MoneyToBigDecimalConvert
import jakarta.persistence.*

@Entity
@Table(name = "payment_orders")
class JpaPaymentOrderEntity (
  // wallet-service 에서는 DB에 PaymentOrder를 기록하는 작업이 없으므로 @GenerateValue 제거
  @Id
  val id: String? = null,

  @Column(name = "seller_id")
  val sellerId: Long,

  @Convert(converter = MoneyToBigDecimalConvert::class)
  val amount: Money,

  @Column(name = "order_id")
  val orderId: String
)