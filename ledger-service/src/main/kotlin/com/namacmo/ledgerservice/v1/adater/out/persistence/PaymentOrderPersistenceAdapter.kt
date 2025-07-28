package com.namacmo.ledgerservice.v1.adater.out.persistence

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.namacmo.ledgerservice.v1.adater.out.persistence.repository.PaymentOrderRepository
import com.namacmo.ledgerservice.v1.application.port.out.LoadPaymentOrderPort
import com.namacmo.ledgerservice.v1.domain.PaymentOrder

@PersistenceAdapter
class PaymentOrderPersistenceAdapter (
  private val paymentOrderRepository: PaymentOrderRepository
) : LoadPaymentOrderPort {

  override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
    return paymentOrderRepository.getPaymentOrders(orderId)
  }
}