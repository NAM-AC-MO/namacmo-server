package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.entity.Product
import reactor.core.publisher.Flux

interface LoadProductPort {

  fun getProducts(cartId: String, productIds: List<String>): Flux<Product>
}