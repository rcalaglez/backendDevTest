package com.example.similarproducts.infrastructure.client;

import com.example.similarproducts.domain.model.ProductDetail;
import com.example.similarproducts.domain.port.out.ProductPort;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
public class ProductApiClient implements ProductPort {

    private final WebClient webClient;
    private final Duration timeoutPerCall;
    private final Cache<String, ProductDetail> cache;

    public ProductApiClient(WebClient webClient, Duration timeoutPerCall, Cache<String, ProductDetail> cache) {
        this.webClient = webClient;
        this.timeoutPerCall = timeoutPerCall != null ? timeoutPerCall : Duration.ofSeconds(2);
        this.cache = cache;
    }

    @Override
    public Mono<List<String>> getSimilarIds(String productId) {
        log.info("Consultando similares para producto {}", productId);
        return webClient.get()
                .uri("/product/{productId}/similarids", productId)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<List<String>>() {})
                .timeout(timeoutPerCall)
                .doOnSuccess(ids -> {
                    if (ids != null) {
                        log.info("Similares de {}: {} IDs encontrados", productId, ids.size());
                    }
                })
                .doOnError(e -> log.warn("Error consultando similares de {}: {}", 
                        productId, e.getMessage()));
    }

    @Override
    public Mono<ProductDetail> getProduct(String productId) {
        ProductDetail cached = cache.getIfPresent(productId);
        if (cached != null) {
            log.debug("Cache HIT para producto {}", productId);
            return Mono.just(cached);
        }
        log.debug("Cache MISS para producto {}", productId);

        return webClient.get()
                .uri("/product/{productId}", productId)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.empty()
                )
                .bodyToMono(ProductDetail.class)
                .timeout(timeoutPerCall)
                .doOnSuccess(product -> {
                    if (product != null) {
                        cache.put(productId, product);
                        log.debug("Producto {} cacheado", productId);
                    }
                })
                .doOnError(e -> log.warn("Error consultando producto {}: {}", 
                        productId, e.getMessage()));
    }
}
