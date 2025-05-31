package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.model.Product;
import com.network.franchise.domain.spi.AddProductServicePort;
import reactor.core.publisher.Mono;

public class AddProductUseCase implements AddProductServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;

    public AddProductUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
    }

    @Override
    public Mono<Product> addProduct(Product request) {
        return Mono.just(request)
                .filter(product -> product.getName() != null && !product.getName().isBlank() && product.getBranchId() != null)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.MISSING_REQUIRED_FIELD)))
                .flatMap(product -> appPersistenceAdapterPort.existsInProductByBranchId(product.getBranchId())
                .flatMap(existsInBranch -> {
                    if (!existsInBranch) return Mono.error(new NotFoundException(TechnicalMessage.NOT_FOUND, "Branch ID: " + product.getBranchId()));
                    return appPersistenceAdapterPort.existsByNameAndBranchId(product.getName(), product.getBranchId())
                            .flatMap(nameExists -> {
                                if (nameExists) return Mono.error(new DuplicateException(TechnicalMessage.ALREADY_EXISTS));
                                return appPersistenceAdapterPort.existsBranchesByIdExists(product.getBranchId())
                                        .flatMap(branchExists -> {
                                            if (!branchExists) return Mono.error(new NotFoundException(TechnicalMessage.NOT_FOUND , "Branch ID: " + product.getBranchId()));
                                            return appPersistenceAdapterPort.addProduct(product);
                                        });
                            });
                }));
    }
}
