package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.payment.walletservice.v1.domain.model.PaymentOrder

interface PaymentOrderRepository {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}