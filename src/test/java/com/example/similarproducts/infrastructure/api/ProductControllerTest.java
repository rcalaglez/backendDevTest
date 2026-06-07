package com.example.similarproducts.infrastructure.api;

import com.example.similarproducts.domain.model.ProductDetail;
import com.example.similarproducts.domain.port.in.GetSimilarProductsUseCase;
import com.example.similarproducts.infrastructure.api.mapper.ProductDetailMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetSimilarProductsUseCase getSimilarProductsUseCase;

    @Autowired
    private ProductDetailMapper mapper;

    @Test
    void shouldReturn200WithProducts() {
        List<ProductDetail> products = List.of(
                new ProductDetail("2", "Dress", 19.99, true),
                new ProductDetail("3", "Blazer", 29.99, false)
        );
        when(getSimilarProductsUseCase.execute("1"))
                .thenReturn(Mono.just(products));

        webTestClient.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("2")
                .jsonPath("$[0].name").isEqualTo("Dress")
                .jsonPath("$[0].price").isEqualTo(19.99)
                .jsonPath("$[0].availability").isEqualTo(true)
                .jsonPath("$[1].id").isEqualTo("3")
                .jsonPath("$[1].availability").isEqualTo(false);
    }

    @Test
    void shouldReturn404WhenNoSimilarProducts() {
        when(getSimilarProductsUseCase.execute("1"))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturn404WhenEmptyList() {
        when(getSimilarProductsUseCase.execute("1"))
                .thenReturn(Mono.just(List.of()));

        webTestClient.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnSingleProduct() {
        List<ProductDetail> products = List.of(
                new ProductDetail("2", "Dress", 19.99, true)
        );
        when(getSimilarProductsUseCase.execute("1"))
                .thenReturn(Mono.just(products));

        webTestClient.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].id").isEqualTo("2")
                .jsonPath("$[0].name").isEqualTo("Dress");
    }
}
