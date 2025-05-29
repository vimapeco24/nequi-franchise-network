package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class DeleteProductUseCaseTest {

    private AppPersistenceAdapterPort appPersistenceAdapterPort;
    private DeleteProductUseCase deleteProductUseCase;

    @BeforeEach
    void setUp() {
        appPersistenceAdapterPort = mock(AppPersistenceAdapterPort.class);
        deleteProductUseCase = new DeleteProductUseCase(appPersistenceAdapterPort);
    }

    @Test
    void deleteProduct_success() {
        Long branchId = 1L;
        Long productId = 10L;

        when(appPersistenceAdapterPort.existsByProductId(productId)).thenReturn(Mono.just(true));
        when(appPersistenceAdapterPort.existsBranchesByIdExists(branchId)).thenReturn(Mono.just(true));
        when(appPersistenceAdapterPort.deleteProduct(branchId, productId)).thenReturn(Mono.empty());

        StepVerifier.create(deleteProductUseCase.deleteProduct(branchId, productId))
                .verifyComplete();

        verify(appPersistenceAdapterPort).deleteProduct(branchId, productId);
    }

    @Test
    void deleteProduct_productNotExists() {
        Long branchId = 1L;
        Long productId = 99L;

        when(appPersistenceAdapterPort.existsByProductId(productId)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteProductUseCase.deleteProduct(branchId, productId))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Product ID: 99 does not exist"))
                .verify();

        verify(appPersistenceAdapterPort, never()).deleteProduct(any(), any());
    }

    @Test
    void deleteProduct_branchNotExists() {
        Long branchId = 2L;
        Long productId = 20L;

        when(appPersistenceAdapterPort.existsByProductId(productId)).thenReturn(Mono.just(true));
        when(appPersistenceAdapterPort.existsBranchesByIdExists(branchId)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteProductUseCase.deleteProduct(branchId, productId))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Branch ID: 2 does not exist"))
                .verify();

        verify(appPersistenceAdapterPort, never()).deleteProduct(any(), any());
    }
}
