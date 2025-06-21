package com.payment.walletservice.v1.adapter.out.persistence

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.payment.walletservice.v1.adapter.out.persistence.repository.PaymentOrderRepository
import com.payment.walletservice.v1.application.port.out.LoadPaymentOrderPort
import com.payment.walletservice.v1.domain.model.PaymentOrder

@PersistenceAdapter
class PaymentOrderPersistenceAdapter (
  private val paymentOrderRepository: PaymentOrderRepository
) : LoadPaymentOrderPort {

  override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
    return paymentOrderRepository.getPaymentOrders(orderId)
  }
}