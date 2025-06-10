package com.namacmo.paymentservice.v1.adapter.out.web.product.client

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.entity.Product
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class MockProductClient : ProductClient {

  override fun getProducts(cartId: String, productIds: List<String>): Flux<Product> {
    return Flux.fromIterable(productIds).map { id ->
      Product(
        id = id,
        amount = Money((id.toInt() * 10000).toString()),
        quantity = 2,
        name = "test_product_$id",
        sellerId = "1"
      )
    }
  }
}