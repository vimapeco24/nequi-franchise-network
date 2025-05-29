package com.network.franchise.domain.api;

import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import io.r2dbc.spi.Result;
import reactor.core.publisher.Mono;

public interface FranchisePersistenceAdapterPort {
    Mono<FranchiseEntity> createFranchise(FranchiseEntity franchiseEntity);
    Mono<BranchEntity> addBranch(BranchEntity branchEntity, Long franchiseId);
    Mono<ProductEntity> addProduct(ProductEntity productEntity, Long branchId);

    Mono<ProductEntity> deleteProduct(Long productId, Long branchId);
    Mono<ProductEntity> updateProduct(ProductEntity productEntity, Long productId);
    Mono<BranchEntity> getTopProductsPerBranch(Long franchiseId);

    Mono<Boolean> existsByName(String name);
}
