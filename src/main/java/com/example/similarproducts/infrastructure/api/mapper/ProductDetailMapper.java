package com.example.similarproducts.infrastructure.api.mapper;

import com.example.similarproducts.domain.model.ProductDetail;
import com.example.similarproducts.infrastructure.api.dto.ProductDetailResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDetailMapper {

    public ProductDetailResponse toResponse(ProductDetail domain) {
        if (domain == null) {
            return null;
        }
        return new ProductDetailResponse(
                domain.id(),
                domain.name(),
                domain.price(),
                domain.availability()
        );
    }

    public List<ProductDetailResponse> toResponseList(List<ProductDetail> domainList) {
        if (domainList == null) {
            return List.of();
        }
        return domainList.stream()
                .map(this::toResponse)
                .toList();
    }
}
