package com.example.similarproducts.application;

import com.example.similarproducts.domain.model.ProductDetail;
import com.example.similarproducts.domain.port.out.ProductPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSimilarProductsServiceTest {

    @Mock
    private ProductPort productPort;

    @InjectMocks
    private GetSimilarProductsService service;

    @Test
    void shouldReturnSimilarProducts() {
        when(productPort.getSimilarIds("1"))
                .thenReturn(Mono.just(List.of("2", "3")));
        when(productPort.getProduct("2"))
                .thenReturn(Mono.just(new ProductDetail("2", "Dress", 19.99, true)));
        when(productPort.getProduct("3"))
                .thenReturn(Mono.just(new ProductDetail("3", "Blazer", 29.99, false)));

        StepVerifier.create(service.execute("1"))
                .assertNext(products -> {
                    assertEquals(2, products.size());
                    assertEquals("2", products.get(0).id());
                    assertEquals("3", products.get(1).id());
                })
                .verifyComplete();
    }

    @Test
    void shouldOmitProductsThatReturnEmpty() {
        when(productPort.getSimilarIds("1"))
                .thenReturn(Mono.just(List.of("2", "99")));
        when(productPort.getProduct("2"))
                .thenReturn(Mono.just(new ProductDetail("2", "Dress", 19.99, true)));
        when(productPort.getProduct("99"))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.execute("1"))
                .assertNext(products -> {
                    assertEquals(1, products.size());
                    assertEquals("2", products.get(0).id());
                })
                .verifyComplete();
    }

    @Test
    void shouldOmitProductsThatError() {
        when(productPort.getSimilarIds("1"))
                .thenReturn(Mono.just(List.of("2", "3")));
        when(productPort.getProduct("2"))
                .thenReturn(Mono.just(new ProductDetail("2", "Dress", 19.99, true)));
        when(productPort.getProduct("3"))
                .thenReturn(Mono.error(new RuntimeException("timeout")));

        StepVerifier.create(service.execute("1"))
                .assertNext(products -> {
                    assertEquals(1, products.size());
                    assertEquals("2", products.get(0).id());
                })
                .verifyComplete();
    }

    @Test
    void shouldPropagateErrorWhenSimilarIdsFails() {
        when(productPort.getSimilarIds("1"))
                .thenReturn(Mono.error(new RuntimeException("service down")));

        StepVerifier.create(service.execute("1"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldReturnEmptyListWhenAllProductsFail() {
        when(productPort.getSimilarIds("1"))
                .thenReturn(Mono.just(List.of("2", "3")));
        when(productPort.getProduct("2"))
                .thenReturn(Mono.error(new RuntimeException("error")));
        when(productPort.getProduct("3"))
                .thenReturn(Mono.error(new RuntimeException("error")));

        StepVerifier.create(service.execute("1"))
                .assertNext(products -> assertTrue(products.isEmpty()))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyListForEmptySimilarIds() {
        when(productPort.getSimilarIds("1"))
                .thenReturn(Mono.just(List.of()));

        StepVerifier.create(service.execute("1"))
                .assertNext(products -> assertTrue(products.isEmpty()))
                .verifyComplete();
    }

    @Test
    void fallbackShouldReturnEmptyList() {
        StepVerifier.create(service.fallback("1", new RuntimeException("test")))
                .assertNext(products -> assertTrue(products.isEmpty()))
                .verifyComplete();
    }
}
