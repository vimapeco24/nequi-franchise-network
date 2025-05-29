package com.network.franchise.domain.usecase.command;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.NotFoundException;
import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.domain.mapper.ProductsDomainMapper;
import com.network.franchise.domain.model.Product;
import com.network.franchise.domain.spi.UpdateStockServicePort;
import reactor.core.publisher.Mono;

public class UpdateStockUseCase implements UpdateStockServicePort {

    private final AppPersistenceAdapterPort appPersistenceAdapterPort;
    private final ProductsDomainMapper mapper;

    public UpdateStockUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort, ProductsDomainMapper mapper) {
        this.appPersistenceAdapterPort = appPersistenceAdapterPort;
        this.mapper = mapper;
    }

    @Override
    public Mono<CreateProductResponseDto> updateStock(Product product) {
        return appPersistenceAdapterPort.findProductById(product.getId())
                .switchIfEmpty(Mono.error(new NotFoundException(
                        TechnicalMessage.NOT_FOUND, "Product ID: " + product.getId() + " does not exist")))
                .flatMap(existing -> {
                    existing.setStock(product.getStock());

                    return appPersistenceAdapterPort.updateProduct(existing, product.getId())
                            .map(mapper::toDomainFromProductEntity);
                })
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BAD_REQUEST)));
    }
}
