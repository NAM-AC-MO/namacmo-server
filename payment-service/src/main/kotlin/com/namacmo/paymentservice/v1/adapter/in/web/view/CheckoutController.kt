package com.namacmo.paymentservice.v1.adapter.`in`.web.view

import com.namacmo.appcommon.hexagonal.WebAdapter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@WebAdapter
@Controller
@RequestMapping("/view/v1/toss")
class CheckoutController {

    @GetMapping("/checkout")
    fun checkout(): Mono<String> {
        return Mono.just("checkout")
    }
}