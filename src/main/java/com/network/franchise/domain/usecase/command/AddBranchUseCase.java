package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.mapper.BranchesDtoMapper;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.domain.spi.AddBranchServicePort;
import reactor.core.publisher.Mono;

public class AddBranchUseCase implements AddBranchServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;
    private final BranchesDtoMapper mapper;

    public AddBranchUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort, BranchesDtoMapper mapper) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
        this.mapper = mapper;
    }

    @Override
    public Mono<CreateBranchResponseDto> addBranch(Branch branch) {
        if (branch.getName() == null || branch.getName().isBlank() || branch.getFranchiseId() == null || branch.getFranchiseId() <= 0) {
            return Mono.error(new BusinessException(TechnicalMessage.MISSING_REQUIRED_FIELD));
        }

        return appPersistenceAdapterPort.existsInBranchByFranchiseId(branch.getFranchiseId())
                .flatMap(exists -> {
                            if (!exists) return Mono.error(new BusinessException(TechnicalMessage.NOT_FOUND));
                            return appPersistenceAdapterPort.existsByBranchName(branch.getName())
                                    .filter(existsByName -> !existsByName)
                                    .flatMap(existsByName -> appPersistenceAdapterPort.existsFranchiseByIdExists(branch.getFranchiseId())
                                            .flatMap(existsFranchise -> {
                                                if (!existsFranchise) return Mono.error(new BusinessException(TechnicalMessage.NOT_FOUND));
                                                return appPersistenceAdapterPort.addBranch(mapper.toEntityFromDomainBranch(branch))
                                                        .map(mapper::toDomainFromBranchEntity);
                                            }))
                                    .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.ALREADY_EXISTS)));
                        });
    }
}
