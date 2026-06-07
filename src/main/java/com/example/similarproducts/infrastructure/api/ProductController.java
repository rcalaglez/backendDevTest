package com.example.similarproducts.infrastructure.api;

import com.example.similarproducts.domain.port.in.GetSimilarProductsUseCase;
import com.example.similarproducts.infrastructure.api.dto.ProductDetailResponse;
import com.example.similarproducts.infrastructure.api.mapper.ProductDetailMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ProductController {

    private final GetSimilarProductsUseCase getSimilarProductsUseCase;
    private final ProductDetailMapper mapper;

    public ProductController(GetSimilarProductsUseCase getSimilarProductsUseCase,
                             ProductDetailMapper mapper) {
        this.getSimilarProductsUseCase = getSimilarProductsUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/product/{productId}/similar")
    public Mono<ResponseEntity<List<ProductDetailResponse>>> getSimilarProducts(
            @PathVariable String productId) {
        return getSimilarProductsUseCase.execute(productId)
                .map(mapper::toResponseList)
                .filter(products -> !products.isEmpty())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
