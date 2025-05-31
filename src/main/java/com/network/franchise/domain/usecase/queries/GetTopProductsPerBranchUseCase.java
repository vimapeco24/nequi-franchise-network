package com.network.franchise.domain.usecase.queries;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.dto.response.newtop.TopProductByBranchByFranchiseResponseDto;
import com.network.franchise.domain.spi.GetTopProductsPerBranchServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GetTopProductsPerBranchUseCase implements GetTopProductsPerBranchServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;

    public GetTopProductsPerBranchUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
    }

    // TODO: obtener lista de producto top de cada sucursal de la franquicia solicitada.
    @Override
    public Flux<TopProductByBranchByFranchiseResponseDto> getTopProductsPerBranch(Long franchiseId) {
        return appPersistenceAdapterPort.findByFranchiseId(franchiseId)
                .flatMapMany(branch -> appPersistenceAdapterPort.getTopProductsPerBranch(franchiseId))
                //.switchIfEmpty(Mono.empty());
                .switchIfEmpty(Flux.error(new NotFoundException(TechnicalMessage.NOT_FOUND, "Franchise ID: " + franchiseId + " does not exist")));
    }
}
