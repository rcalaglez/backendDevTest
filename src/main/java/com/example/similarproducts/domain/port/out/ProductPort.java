package com.example.similarproducts.domain.port.out;

import com.example.similarproducts.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductPort {

    Mono<List<String>> getSimilarIds(String productId);

    Mono<ProductDetail> getProduct(String productId);
}
