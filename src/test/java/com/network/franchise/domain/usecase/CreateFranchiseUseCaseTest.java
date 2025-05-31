package com.network.franchise.domain.usecase;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.domain.mapper.FranchiseDtoMapper;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import com.network.franchise.domain.usecase.command.CreateFranchiseUseCase;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateFranchiseUseCaseTest {

    private AppPersistenceAdapterPort appPersistenceAdapterPort;
    private FranchiseDtoMapper mapper;
    private CreateFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        appPersistenceAdapterPort = mock(AppPersistenceAdapterPort.class);
        mapper = mock(FranchiseDtoMapper.class);
        useCase = new CreateFranchiseUseCase(appPersistenceAdapterPort, mapper);
    }

    @Test
    void createTechnology_shouldReturnFranchise_whenNotExists() {
        // Arrange
        Franchise request = Franchise.builder().name("Franchise A").build();
        FranchiseEntity entity = FranchiseEntity.builder().name("Franchise A").build();
        CreateFranchiseResponseDto result = CreateFranchiseResponseDto.builder().name("Franchise A").build();

        when(appPersistenceAdapterPort.existsFranchiseByName("Franchise A"))
        .thenReturn(Mono.just(false));
        when(mapper.toEntityFromDomainFranchise(request)).thenReturn(entity);
        when(appPersistenceAdapterPort.createFranchise(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDomainFromFranchiseEntity(entity)).thenReturn(result);

        // Act & Assert
        StepVerifier.create(useCase.createTechnology(request))
                .expectNext(result)
                .verifyComplete();

        verify(appPersistenceAdapterPort).createFranchise(entity);
    }

    @Test
    void createTechnology_shouldReturnError_whenAlreadyExists() {
        // Arrange
        Franchise request = Franchise.builder().name("Franchise B").build();

        when(appPersistenceAdapterPort.existsFranchiseByName("Franchise B"))
                .thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(useCase.createTechnology(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof DuplicateException)
                .verify();

        verify(appPersistenceAdapterPort, never()).createFranchise(any());
    }

    @Test
    void createTechnology_shouldReturnError_whenExistsReturnsEmpty() {
        // Arrange
        Franchise request = Franchise.builder().name("Franchise C").build();

        when(appPersistenceAdapterPort.existsFranchiseByName("Franchise C"))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.createTechnology(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getTechnicalMessage() == TechnicalMessage.BAD_REQUEST)
                .verify();

        verify(appPersistenceAdapterPort, never()).createFranchise(any());
    }
}
