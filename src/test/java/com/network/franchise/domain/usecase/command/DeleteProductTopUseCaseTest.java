package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.spi.DeleteProductServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class DeleteProductTopUseCaseTest {

    private AppPersistenceAdapterPort appPersistenceAdapterPort;
    private DeleteProductServicePort deleteProductUseCase;

    private final Long productId = 1L;
    private final Long branchId = 100L;

    @BeforeEach
    void setUp() {
        appPersistenceAdapterPort = mock(AppPersistenceAdapterPort.class);
        deleteProductUseCase = new DeleteProductUseCase(appPersistenceAdapterPort);
    }

    @Test
    void shouldDeleteProductWhenProductAndBranchExist() {
        when(appPersistenceAdapterPort.existsByProductId(productId)).thenReturn(Mono.just(true));
        when(appPersistenceAdapterPort.existsBranchesByIdExists(branchId)).thenReturn(Mono.just(true));
        when(appPersistenceAdapterPort.deleteProduct(branchId, productId)).thenReturn(Mono.empty());

        StepVerifier.create(deleteProductUseCase.deleteProduct(branchId, productId))
                .verifyComplete();

        verify(appPersistenceAdapterPort).existsByProductId(productId);
        verify(appPersistenceAdapterPort).existsBranchesByIdExists(branchId);
        verify(appPersistenceAdapterPort).deleteProduct(branchId, productId);
    }

    @Test
    void shouldReturnErrorWhenProductDoesNotExist() {
        when(appPersistenceAdapterPort.existsByProductId(productId)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteProductUseCase.deleteProduct(branchId, productId))
                .expectErrorSatisfies(error -> {
                    assert error instanceof NotFoundException;
                    assert error.getMessage().contains("Product ID: " + productId + " does not exist");
                })
                .verify();

        verify(appPersistenceAdapterPort).existsByProductId(productId);
        verify(appPersistenceAdapterPort, never()).existsBranchesByIdExists(any());
        verify(appPersistenceAdapterPort, never()).deleteProduct(any(), any());
    }

    @Test
    void shouldReturnErrorWhenBranchDoesNotExist() {
        when(appPersistenceAdapterPort.existsByProductId(productId)).thenReturn(Mono.just(true));
        when(appPersistenceAdapterPort.existsBranchesByIdExists(branchId)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteProductUseCase.deleteProduct(branchId, productId))
                .expectErrorSatisfies(error -> {
                    assert error instanceof NotFoundException;
                    assert error.getMessage().contains("Branch ID: " + branchId + " does not exist");
                })
                .verify();

        verify(appPersistenceAdapterPort).existsByProductId(productId);
        verify(appPersistenceAdapterPort).existsBranchesByIdExists(branchId);
        verify(appPersistenceAdapterPort, never()).deleteProduct(any(), any());
    }
}
