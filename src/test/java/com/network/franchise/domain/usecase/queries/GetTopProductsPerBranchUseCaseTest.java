package com.network.franchise.domain.usecase.queries;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GetTopProductsPerBranchUseCaseTest {

    private AppPersistenceAdapterPort appPersistenceAdapterPort;
    private GetTopProductsPerBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        appPersistenceAdapterPort = Mockito.mock(AppPersistenceAdapterPort.class);
        useCase = new GetTopProductsPerBranchUseCase(appPersistenceAdapterPort);
    }

    @Test
    void shouldReturnNotFound_whenFranchiseDoesNotExist() {
        Long franchiseId = 99L;

        Mockito.when(appPersistenceAdapterPort.findByFranchiseId(franchiseId))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.getTopProductsPerBranch(franchiseId))
                .expectErrorSatisfies(error -> {
                    assert error instanceof NotFoundException;
                    assert ((NotFoundException) error).getMessage().contains("Franchise ID: " + franchiseId);
                })
                .verify();

        Mockito.verify(appPersistenceAdapterPort).findByFranchiseId(franchiseId);
        Mockito.verify(appPersistenceAdapterPort, Mockito.never()).getTopProductsPerBranch(Mockito.anyLong());
    }
}
