package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.spi.DeleteProductServicePort;
import reactor.core.publisher.Mono;

public class DeleteProductUseCase implements DeleteProductServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;

    public DeleteProductUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
    }

    @Override
    public Mono<Void> deleteProduct(Long branchId, Long productId) {
        return appPersistenceAdapterPort.existsByProductId(productId)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new IllegalArgumentException("Product ID: " + productId + " does not exist"));
                    return appPersistenceAdapterPort.existsBranchesByIdExists(branchId)
                            .flatMap(branchExists -> {
                                if (!branchExists) return Mono.error(new IllegalArgumentException("Branch ID: " + branchId + " does not exist"));
                                return appPersistenceAdapterPort.deleteProduct(branchId, productId);
                            });

                });
    }
}
