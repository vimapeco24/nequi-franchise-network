package com.network.franchise.domain.usecase.queries;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.dto.response.top.TopProductPerBranchDto;
import com.network.franchise.domain.spi.GetTopProductsPerBranchServicePort;
import reactor.core.publisher.Mono;

public class GetTopProductsPerBranchUseCase implements GetTopProductsPerBranchServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;

    public GetTopProductsPerBranchUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
    }

    @Override
    public Mono<TopProductPerBranchDto> getTopProductsPerBranch(Long franchiseId) {
        return appPersistenceAdapterPort.findByFranchiseId(franchiseId)
                .flatMap(branch -> appPersistenceAdapterPort.getTopProductsPerBranch(franchiseId))
                .switchIfEmpty(Mono.error(new NotFoundException(TechnicalMessage.NOT_FOUND, "Franchise ID: " + franchiseId + " does not exist")));
    }
}
