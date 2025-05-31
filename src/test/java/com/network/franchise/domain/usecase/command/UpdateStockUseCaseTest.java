package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.infrastructure.inbound.mapper.ProductsMapper;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateStockUseCaseTest {

    @Mock
    private AppPersistenceAdapterPort appPersistenceAdapterPort;

    @Mock
    private ProductsMapper mapper;

    @InjectMocks
    private UpdateStockUseCase updateStockUseCase;

    @Test
    void updateStock_whenProductExists_shouldUpdateAndReturnResponse() {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setStock(20);

        ProductEntity existingEntity = new ProductEntity();
        existingEntity.setId(1L);
        existingEntity.setStock(10);

        CreateProductResponseDto responseDto = new CreateProductResponseDto();
        responseDto.setId(1L);
        responseDto.setStock(20L);

        when(appPersistenceAdapterPort.findProductById(1L)).thenReturn(Mono.just(existingEntity));
        when(appPersistenceAdapterPort.updateProduct(existingEntity, 1L)).thenReturn(Mono.just(existingEntity));
        when(mapper.toDomainFromProductEntity(existingEntity)).thenReturn(responseDto);

        // When
        StepVerifier.create(updateStockUseCase.updateStock(product))
                .expectNextMatches(dto -> dto.getId().equals(1L) && dto.getStock() == 20)
                .verifyComplete();

        // Then
        verify(appPersistenceAdapterPort).findProductById(1L);
        verify(appPersistenceAdapterPort).updateProduct(existingEntity, 1L);
        verify(mapper).toDomainFromProductEntity(existingEntity);
    }

    @Test
    void updateStock_whenProductNotFound_shouldReturnNotFoundError() {
        // Given
        Product product = new Product();
        product.setId(99L);
        product.setStock(5);

        when(appPersistenceAdapterPort.findProductById(99L)).thenReturn(Mono.empty());

        // When
        StepVerifier.create(updateStockUseCase.updateStock(product))
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().contains("Product ID: 99 does not exist"))
                .verify();

        // Then
        verify(appPersistenceAdapterPort).findProductById(99L);
        verify(appPersistenceAdapterPort, never()).updateProduct(any(), anyLong());
        verify(mapper, never()).toDomainFromProductEntity(any());
    }

    @Test
    void updateStock_whenUpdateProductReturnsEmpty_shouldReturnBadRequest() {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setStock(30);

        ProductEntity existingEntity = new ProductEntity();
        existingEntity.setId(1L);
        existingEntity.setStock(10);

        when(appPersistenceAdapterPort.findProductById(1L)).thenReturn(Mono.just(existingEntity));
        when(appPersistenceAdapterPort.updateProduct(existingEntity, 1L)).thenReturn(Mono.empty());

        // When
        StepVerifier.create(updateStockUseCase.updateStock(product))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException)
                .verify();

        // Then
        verify(appPersistenceAdapterPort).findProductById(1L);
        verify(appPersistenceAdapterPort).updateProduct(existingEntity, 1L);
        verify(mapper, never()).toDomainFromProductEntity(any());
    }
}
