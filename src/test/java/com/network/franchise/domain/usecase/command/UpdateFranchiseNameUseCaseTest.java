package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.infrastructure.mapper.FranchiseDtoMapper;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.spi.UpdateFranchiseNameServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class UpdateFranchiseNameUseCaseTest {

    private AppPersistenceAdapterPort appPersistenceAdapterPort;
    private FranchiseDtoMapper franchiseMapper;
    private UpdateFranchiseNameServicePort useCase;

    @BeforeEach
    void setup() {
        appPersistenceAdapterPort = mock(AppPersistenceAdapterPort.class);
        useCase = new UpdateFranchiseNameUseCase(appPersistenceAdapterPort);
    }

//    @Test
//    void shouldReturnErrorWhenNameIsMissing() {
//        Franchise request = new Franchise();
//        request.setName("");
//
//        StepVerifier.create(useCase.updateFranchiseName(request))
//                .expectErrorMatches(throwable ->
//                        throwable instanceof BusinessException)
//                .verify();
//    }

    @Test
    void shouldReturnErrorWhenNameAlreadyExists() {
        Franchise request = new Franchise();
        request.setId(1L);
        request.setName("Existing Franchise");

        when(appPersistenceAdapterPort.existsFranchiseByName("Existing Franchise")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.updateFranchiseName(request))
                .expectError(DuplicateException.class)
                .verify();

        verify(appPersistenceAdapterPort).existsFranchiseByName("Existing Franchise");
        verifyNoMoreInteractions(appPersistenceAdapterPort);
    }

//    @Test
//    void shouldUpdateFranchiseSuccessfully() {
//        Franchise request = new Franchise();
//        request.setId(1L);
//        request.setName("New Franchise");
//
//        FranchiseEntity entity = new FranchiseEntity();
//        entity.setId(1L);
//        entity.setName("New Franchise");
//
//        when(appPersistenceAdapterPort.existsFranchiseByName("New Franchise")).thenReturn(Mono.just(false));
//        when(franchiseMapper.toEntityFromDomainFranchise(request)).thenReturn(entity);
//        when(appPersistenceAdapterPort.updateFranchiseName(entity)).thenReturn(Mono.just(1L));
//
//        StepVerifier.create(useCase.updateFranchiseName(request))
//                .expectNext(1L)
//                .verifyComplete();
//
//        verify(appPersistenceAdapterPort).existsFranchiseByName("New Franchise");
//        verify(appPersistenceAdapterPort).updateFranchiseName(entity);
//    }

//    @Test
//    void shouldReturnErrorIfUpdateReturnsEmpty() {
//        Franchise request = new Franchise();
//        request.setId(1L);
//        request.setName("Franchise");
//
//        FranchiseEntity entity = new FranchiseEntity();
//        entity.setId(1L);
//        entity.setName("Franchise");
//
//        when(appPersistenceAdapterPort.existsFranchiseByName("Franchise")).thenReturn(Mono.just(false));
//        when(franchiseMapper.toEntityFromDomainFranchise(request)).thenReturn(entity);
//        when(appPersistenceAdapterPort.updateFranchiseName(entity)).thenReturn(Mono.empty());
//
//        StepVerifier.create(useCase.updateFranchiseName(request))
//                .expectError(BusinessException.class)
//                .verify();
//    }
}
