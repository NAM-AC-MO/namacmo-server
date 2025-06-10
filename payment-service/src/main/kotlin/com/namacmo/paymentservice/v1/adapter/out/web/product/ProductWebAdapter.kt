package com.namacmo.paymentservice.v1.adapter.out.web.product

import com.namacmo.appcommon.hexagonal.WebAdapter
import com.namacmo.paymentservice.v1.adapter.out.web.product.client.ProductClient
import com.namacmo.paymentservice.v1.application.port.out.LoadProductPort
import com.namacmo.paymentservice.v1.domain.entity.Product
import reactor.core.publisher.Flux

@WebAdapter
class ProductWebAdapter(
    private val productClient: ProductClient
): LoadProductPort {
    override fun getProducts(cartId: String, productIds: List<String>): Flux<Product> {
        return productClient.getProducts(cartId, productIds)
    }
}