package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.DuplicateException;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.domain.mapper.ProductDtoMapper;
import com.network.franchise.domain.model.Product;
import com.network.franchise.domain.spi.AddProductServicePort;
import reactor.core.publisher.Mono;

public class AddProductUseCase implements AddProductServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;
    private final ProductDtoMapper mapper;

    public AddProductUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort, ProductDtoMapper mapper) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
        this.mapper = mapper;
    }

    @Override
    public Mono<CreateProductResponseDto> addProduct(Product product) {

        if (product.getName() == null || product.getName().isBlank() || product.getBranchId() == null || product.getBranchId() <= 0) {
            return Mono.error(new BusinessException(TechnicalMessage.MISSING_REQUIRED_FIELD));
        }

        return appPersistenceAdapterPort.existsInProductByBranchId(product.getBranchId())
                .flatMap(existsInBranch -> {
                    if (!existsInBranch) return Mono.error(new NotFoundException(TechnicalMessage.NOT_FOUND, "Branch ID: " + product.getBranchId()));

                    return appPersistenceAdapterPort.existsByProductName(product.getName())
                            .flatMap(nameExists -> {
                                if (nameExists) return Mono.error(new DuplicateException(TechnicalMessage.ALREADY_EXISTS));

                                return appPersistenceAdapterPort.existsBranchesByIdExists(product.getBranchId())
                                        .flatMap(branchExists -> {
                                            if (!branchExists) return Mono.error(new BusinessException(TechnicalMessage.NOT_FOUND));

                                            return appPersistenceAdapterPort.addProduct(mapper.toEntityFromDomainProduct(product))
                                                    .map(mapper::toDomainFromProductEntity);
                                        });
                            });
                });
    }
}
