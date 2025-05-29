package com.network.franchise.domain.api;

import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import reactor.core.publisher.Mono;

public interface AppPersistenceAdapterPort {
    Mono<FranchiseEntity> createFranchise(FranchiseEntity franchiseEntity);
    Mono<BranchEntity> addBranch(BranchEntity branchEntity);

    Mono<ProductEntity> addProduct(ProductEntity productEntity, Long branchId);
    Mono<ProductEntity> deleteProduct(Long productId, Long branchId);
    Mono<ProductEntity> updateProduct(ProductEntity productEntity, Long productId);
    Mono<BranchEntity> getTopProductsPerBranch(Long franchiseId);

    Mono<Boolean> existsFranchiseByName(String name);
    //Mono<BranchEntity> findByFranchiseId(Long franchiseId);

    Mono<Boolean> existsInBranchByFranchiseId(Long franchiseId);

    Mono<Boolean> existsByBranchName(String name);

    Mono<Boolean> existsFranchiseByIdExists(Long id);
}
