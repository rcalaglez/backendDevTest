package com.example.similarproducts.infrastructure.client;

import com.example.similarproducts.domain.model.ProductDetail;
import com.example.similarproducts.domain.port.out.ProductPort;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class ProductApiClient implements ProductPort {

    private final WebClient webClient;
    private final Duration timeoutPerCall;

    public ProductApiClient(WebClient webClient, Duration timeoutPerCall) {
        this.webClient = webClient;
        this.timeoutPerCall = timeoutPerCall != null ? timeoutPerCall : Duration.ofSeconds(2);
    }

    @Override
    public Mono<List<String>> getSimilarIds(String productId) {
        return webClient.get()
                .uri("/product/{productId}/similarids", productId)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<List<String>>() {})
                .timeout(timeoutPerCall);
    }

    @Override
    public Mono<ProductDetail> getProduct(String productId) {
        return webClient.get()
                .uri("/product/{productId}", productId)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.empty()
                )
                .bodyToMono(ProductDetail.class)
                .timeout(timeoutPerCall);
    }
}
