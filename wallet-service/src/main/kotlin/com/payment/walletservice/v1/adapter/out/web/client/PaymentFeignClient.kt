package com.payment.walletservice.v1.adapter.out.web.client

import com.payment.walletservice.v1.adapter.out.web.response.PaymentOrderResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "payment-service", url = "localhost:9191/api/v1/payment")
interface PaymentFeignClient {
    @GetMapping("/orders/{orderId}")
    fun getPaymentOrders(@PathVariable orderId: String): List<PaymentOrderResponse>

}