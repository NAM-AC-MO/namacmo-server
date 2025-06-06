package com.namacmo.paymentservice.v1.adapter.`in`.web.view

import com.namacmo.appcommon.hexagonal.WebAdapter
import com.namacmo.appcommon.utils.IdempotencyCreator
import com.namacmo.paymentservice.v1.adapter.`in`.web.request.CheckoutRequest
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutCommand
import com.namacmo.paymentservice.v1.application.port.`in`.CheckoutUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@WebAdapter
@Controller
@RequestMapping("/view/v1/toss")
class CheckoutController(
    val checkoutUseCase: CheckoutUseCase
) {

    @GetMapping("/checkout")
    fun checkout(request: CheckoutRequest, model: Model): Mono<String> {
        val command = CheckoutCommand(
            cartId = request.cartId,
            buyerId = request.buyerId,
            productIds = request.productIds,
            idempotencyKey = IdempotencyCreator.create(request)
        )

        return checkoutUseCase.checkout(command)
            .map {
                model.addAttribute("orderId", it.orderId)
                model.addAttribute("orderName", it.orderName)
                model.addAttribute("amount", it.amount.toStringValue())
                "checkout"
            }
    }
}