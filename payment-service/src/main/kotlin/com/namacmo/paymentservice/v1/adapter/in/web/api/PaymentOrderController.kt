package com.namacmo.paymentservice.v1.adapter.`in`.web.api

import com.namacmo.appcommon.hexagonal.WebAdapter
import com.namacmo.paymentservice.v1.adapter.`in`.web.response.ApiResponse
import com.namacmo.paymentservice.v1.adapter.`in`.web.response.PaymentOrderResponse
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentOrderUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@WebAdapter
@RestController
@RequestMapping("/api/v1/payment")
class PaymentOrderController(
    private val paymentOrderUseCase: PaymentOrderUseCase
) {

    @GetMapping("/orders/{orderId}")
    fun getPaymentOrders(@PathVariable orderId: String): Mono<List<PaymentOrderResponse>> {
        return paymentOrderUseCase.getPaymentOrders(orderId)
            .flatMapMany { Flux.fromIterable(it.getPaymentOrders()) }
            .map(PaymentOrderResponse::from)
            .collectList()

//        return ApiResponse.with(httpStatus = HttpStatus.OK, message = "ok", data = paymentOrders)
    }
}