package com.example.similarproducts.infrastructure.config;

import com.example.similarproducts.application.GetSimilarProductsService;
import com.example.similarproducts.domain.port.out.ProductPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public GetSimilarProductsService getSimilarProductsService(ProductPort productPort) {
        return new GetSimilarProductsService(productPort);
    }
}
