package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.spi.DeleteProductServicePort;
import reactor.core.publisher.Mono;

public class DeleteProductUseCase implements DeleteProductServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;

    public DeleteProductUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
    }

    @Override
    public Mono<Void> deleteProduct(Long branchId, Long productId) {
        return Mono.just(branchId)
                .filter(branch -> true)
                .filter(product -> true)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.MISSING_REQUIRED_FIELD)))
                .flatMap(product -> appPersistenceAdapterPort.existsByProductId(productId))
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new NotFoundException(TechnicalMessage.NOT_FOUND, "Product ID: " + productId + " does not exist"));
                    return appPersistenceAdapterPort.existsBranchesByIdExists(branchId)
                            .flatMap(branchExists -> {
                                if (!branchExists) return Mono.error(new NotFoundException(TechnicalMessage.NOT_FOUND, "Branch ID: " + branchId + " does not exist"));
                                return appPersistenceAdapterPort.deleteProduct(branchId, productId);
                            });
                });
    }
}
