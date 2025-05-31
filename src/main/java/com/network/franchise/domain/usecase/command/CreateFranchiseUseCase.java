package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.spi.CreateFranchiseServicePort;
import reactor.core.publisher.Mono;

public class CreateFranchiseUseCase implements CreateFranchiseServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;

    public CreateFranchiseUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
    }

    @Override
    public Mono<Franchise> createTechnology(Franchise request) {
        return Mono.just(request)
                .filter(franchise -> franchise.getName() != null && !franchise.getName().isBlank())
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.MISSING_REQUIRED_FIELD)))
                .flatMap(branch -> appPersistenceAdapterPort.existsFranchiseByName(request.getName())
                .flatMap(exists -> {
                    if (exists) return Mono.error(new DuplicateException(TechnicalMessage.ALREADY_EXISTS));
                    return appPersistenceAdapterPort.createFranchise(request);
                })
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BAD_REQUEST))));
    }
}
