package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.domain.mapper.ProductDtoMapper;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AddProductUseCaseTest {

    @Mock
    private AppPersistenceAdapterPort appPersistenceAdapterPort;

    @Mock
    private ProductDtoMapper mapper;

    @InjectMocks
    private AddProductUseCase addProductUseCase;

    private Product validProduct;

    @BeforeEach
    void setUp() {
        validProduct = Product.builder()
                .name("Product 1")
                .branchId(1L)
                .build();
    }

    @Test
    void shouldReturnErrorWhenNameIsNull() {
        Product product = Product.builder().name(null).branchId(1L).build();

        StepVerifier.create(addProductUseCase.addProduct(product))
                .expectErrorMatches(error -> error instanceof BusinessException &&
                        ((BusinessException) error).getTechnicalMessage().equals(TechnicalMessage.MISSING_REQUIRED_FIELD))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenBranchIdIsInvalid() {
        Product product = Product.builder().name("Test").branchId(0L).build();

        StepVerifier.create(addProductUseCase.addProduct(product))
                .expectErrorMatches(error -> error instanceof BusinessException &&
                        ((BusinessException) error).getTechnicalMessage().equals(TechnicalMessage.MISSING_REQUIRED_FIELD))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenBranchDoesNotExist() {
        Mockito.when(appPersistenceAdapterPort.existsInProductByBranchId(1L)).thenReturn(Mono.just(false));

        StepVerifier.create(addProductUseCase.addProduct(validProduct))
                .expectErrorMatches(error -> error instanceof NotFoundException)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenProductNameAlreadyExists() {
        Mockito.when(appPersistenceAdapterPort.existsInProductByBranchId(1L)).thenReturn(Mono.just(true));
        Mockito.when(appPersistenceAdapterPort.existsByProductName("Product 1")).thenReturn(Mono.just(true));

        StepVerifier.create(addProductUseCase.addProduct(validProduct))
                .expectErrorMatches(error -> error instanceof DuplicateException)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenBranchIdNotFound() {
        Mockito.when(appPersistenceAdapterPort.existsInProductByBranchId(1L)).thenReturn(Mono.just(true));
        Mockito.when(appPersistenceAdapterPort.existsByProductName("Product 1")).thenReturn(Mono.just(false));
        Mockito.when(appPersistenceAdapterPort.existsBranchesByIdExists(1L)).thenReturn(Mono.just(false));

        StepVerifier.create(addProductUseCase.addProduct(validProduct))
                .expectErrorMatches(error -> error instanceof BusinessException &&
                        ((BusinessException) error).getTechnicalMessage().equals(TechnicalMessage.NOT_FOUND))
                .verify();
    }

    @Test
    void shouldAddProductSuccessfully() {
        ProductEntity entity = new ProductEntity();
        CreateProductResponseDto responseDto = CreateProductResponseDto.builder()
                .id(1L)
                .name("Product 1")
                .build();

        Mockito.when(appPersistenceAdapterPort.existsInProductByBranchId(1L)).thenReturn(Mono.just(true));
        Mockito.when(appPersistenceAdapterPort.existsByProductName("Product 1")).thenReturn(Mono.just(false));
        Mockito.when(appPersistenceAdapterPort.existsBranchesByIdExists(1L)).thenReturn(Mono.just(true));
        Mockito.when(mapper.toEntityFromDomainProduct(validProduct)).thenReturn(entity);
        Mockito.when(appPersistenceAdapterPort.addProduct(entity)).thenReturn(Mono.just(entity));
        Mockito.when(mapper.toDomainFromProductEntity(entity)).thenReturn(responseDto);

        StepVerifier.create(addProductUseCase.addProduct(validProduct))
                .expectNext(responseDto)
                .verifyComplete();
    }
}
