package com.namacmo.ledgerservice.v1.application.port.out

import com.namacmo.ledgerservice.v1.domain.PaymentOrder

interface LoadPaymentOrderPort {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}