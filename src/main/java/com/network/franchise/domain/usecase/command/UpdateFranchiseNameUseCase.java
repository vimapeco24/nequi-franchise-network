package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.spi.UpdateFranchiseNameServicePort;
import reactor.core.publisher.Mono;

public class UpdateFranchiseNameUseCase implements UpdateFranchiseNameServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;

    public UpdateFranchiseNameUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
    }

    @Override
    public Mono<Long> updateFranchiseName(Franchise request) {
//        if (request.getName() == null || request.getName().isBlank()) {
//            return Mono.error(new BusinessException(TechnicalMessage.MISSING_REQUIRED_FIELD));
//        }
        return appPersistenceAdapterPort.existsFranchiseByName(request.getName())
                .flatMap(exists -> {
                    if (exists) return Mono.error(new DuplicateException(TechnicalMessage.ALREADY_EXISTS));
                    return appPersistenceAdapterPort.updateFranchiseName(request);
                })
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BAD_REQUEST)));
    }
}
