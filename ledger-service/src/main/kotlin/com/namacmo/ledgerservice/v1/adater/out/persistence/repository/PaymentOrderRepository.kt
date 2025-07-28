package com.namacmo.ledgerservice.v1.adater.out.persistence.repository

import com.namacmo.ledgerservice.v1.domain.PaymentOrder

interface PaymentOrderRepository {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}