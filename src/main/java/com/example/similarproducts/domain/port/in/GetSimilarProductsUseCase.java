package com.example.similarproducts.domain.port.in;

import com.example.similarproducts.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GetSimilarProductsUseCase {

    Mono<List<ProductDetail>> execute(String productId);
}
