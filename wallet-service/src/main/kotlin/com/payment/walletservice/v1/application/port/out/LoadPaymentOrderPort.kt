package com.payment.walletservice.v1.application.port.out

import com.payment.walletservice.v1.domain.model.PaymentOrder

interface LoadPaymentOrderPort {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}