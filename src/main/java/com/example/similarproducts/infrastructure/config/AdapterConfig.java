package com.example.similarproducts.infrastructure.config;

import com.example.similarproducts.domain.port.out.ProductPort;
import com.example.similarproducts.infrastructure.client.ProductApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class AdapterConfig {

    @Bean
    public ProductPort productPort(
            WebClient productWebClient,
            @Value("${product-api.timeout-per-call:2s}") Duration timeoutPerCall) {
        return new ProductApiClient(productWebClient, timeoutPerCall);
    }
}
