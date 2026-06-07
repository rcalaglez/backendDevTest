package com.example.similarproducts.application;

import com.example.similarproducts.domain.model.ProductDetail;
import com.example.similarproducts.domain.port.in.GetSimilarProductsUseCase;
import com.example.similarproducts.domain.port.out.ProductPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class GetSimilarProductsService implements GetSimilarProductsUseCase {

    private static final String PRODUCT_API = "productApi";
    private static final int MAX_CONCURRENCY = 10;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    private final ProductPort productPort;
    private final Duration globalTimeout;

    public GetSimilarProductsService(ProductPort productPort) {
        this(productPort, DEFAULT_TIMEOUT);
    }

    public GetSimilarProductsService(ProductPort productPort, Duration globalTimeout) {
        this.productPort = productPort;
        this.globalTimeout = globalTimeout != null ? globalTimeout : DEFAULT_TIMEOUT;
    }

    @Override
    @CircuitBreaker(name = PRODUCT_API, fallbackMethod = "fallback")
    public Mono<List<ProductDetail>> execute(String productId) {
        return productPort.getSimilarIds(productId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(id -> productPort.getProduct(id)
                        .onErrorResume(e -> Mono.empty()), MAX_CONCURRENCY)
                .collectList()
                .timeout(globalTimeout);
    }

    public Mono<List<ProductDetail>> fallback(String productId, Throwable throwable) {
        return Mono.just(Collections.emptyList());
    }
}
