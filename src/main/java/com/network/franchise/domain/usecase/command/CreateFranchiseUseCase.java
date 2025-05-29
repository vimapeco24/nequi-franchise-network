package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.domain.mapper.FranchiseDomainMapper;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.spi.CreateFranchiseServicePort;
import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import reactor.core.publisher.Mono;

public class CreateFranchiseUseCase implements CreateFranchiseServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;
    private final FranchiseDomainMapper mapper;

    public CreateFranchiseUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort, FranchiseDomainMapper mapper) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
        this.mapper = mapper;
    }

    @Override
    public Mono<CreateFranchiseResponseDto> createTechnology(Franchise request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.MISSING_REQUIRED_FIELD));
        }
        return appPersistenceAdapterPort.existsFranchiseByName(request.getName())
                .flatMap(exists -> {
                    if (exists) return Mono.error(new DuplicateException(TechnicalMessage.ALREADY_EXISTS));
                    return appPersistenceAdapterPort.createFranchise(mapper.toEntityFromDomainFranchise(request))
                            .map(mapper::toDomainFromFranchiseEntity);
                })
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BAD_REQUEST)));
    }
}
