package com.namacmo.paymentservice.v1.adapter.out.web.product.client

import com.namacmo.paymentservice.v1.domain.entity.Product
import reactor.core.publisher.Flux

interface ProductClient {
    fun getProducts(cartId: String, productIds: List<String>): Flux<Product>
}