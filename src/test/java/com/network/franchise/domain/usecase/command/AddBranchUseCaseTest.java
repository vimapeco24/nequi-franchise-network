package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.mapper.BranchesDtoMapper;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
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
class AddBranchUseCaseTest {

    @Mock
    private AppPersistenceAdapterPort appPersistenceAdapterPort;

    @Mock
    private BranchesDtoMapper mapper;

    @InjectMocks
    private AddBranchUseCase addBranchUseCase;

    private final Long franchiseId = 1L;
    private final String branchName = "Main Branch";

    private Branch validBranch;

    @BeforeEach
    void setUp() {
        validBranch = Branch.builder()
                .name(branchName)
                .franchiseId(franchiseId)
                .build();
    }

    @Test
    void shouldReturnErrorWhenBranchNameIsNull() {
        Branch branch = Branch.builder().name(null).franchiseId(franchiseId).build();

        StepVerifier.create(addBranchUseCase.addBranch(branch))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenFranchiseIdIsNull() {
        Branch branch = Branch.builder().name("Valid Name").franchiseId(null).build();

        StepVerifier.create(addBranchUseCase.addBranch(branch))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenFranchiseDoesNotExist() {
        Mockito.when(appPersistenceAdapterPort.existsInBranchByFranchiseId(franchiseId))
                .thenReturn(Mono.just(false));

        StepVerifier.create(addBranchUseCase.addBranch(validBranch))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenBranchAlreadyExists() {
        Mockito.when(appPersistenceAdapterPort.existsInBranchByFranchiseId(franchiseId))
                .thenReturn(Mono.just(true));

        Mockito.when(appPersistenceAdapterPort.existsByBranchName(branchName))
                .thenReturn(Mono.just(true)); // already exists

        StepVerifier.create(addBranchUseCase.addBranch(validBranch))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenFranchiseIdDoesNotExistOnSecondCheck() {
        Mockito.when(appPersistenceAdapterPort.existsInBranchByFranchiseId(franchiseId))
                .thenReturn(Mono.just(true));

        Mockito.when(appPersistenceAdapterPort.existsByBranchName(branchName))
                .thenReturn(Mono.just(false)); // not existing, so OK to proceed

        Mockito.when(appPersistenceAdapterPort.existsFranchiseByIdExists(franchiseId))
                .thenReturn(Mono.just(false)); // franchiseId not found

        StepVerifier.create(addBranchUseCase.addBranch(validBranch))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldCreateBranchSuccessfully() {
        BranchEntity branchEntity = new BranchEntity(); // Simulate entity
        CreateBranchResponseDto responseDto = CreateBranchResponseDto.builder().name("Success").build();

        Mockito.when(appPersistenceAdapterPort.existsInBranchByFranchiseId(franchiseId))
                .thenReturn(Mono.just(true));

        Mockito.when(appPersistenceAdapterPort.existsByBranchName(branchName))
                .thenReturn(Mono.just(false));

        Mockito.when(appPersistenceAdapterPort.existsFranchiseByIdExists(franchiseId))
                .thenReturn(Mono.just(true));

        Mockito.when(appPersistenceAdapterPort.addBranch(Mockito.any()))
                .thenReturn(Mono.just(branchEntity));

        Mockito.when(mapper.toEntityFromDomainBranch(validBranch))
                .thenReturn(branchEntity);

        Mockito.when(mapper.toDomainFromBranchEntity(branchEntity))
                .thenReturn(responseDto);

        StepVerifier.create(addBranchUseCase.addBranch(validBranch))
                .expectNext(responseDto)
                .verifyComplete();
    }
}
