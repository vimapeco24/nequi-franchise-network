package com.network.franchise.domain.api;

import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import reactor.core.publisher.Mono;

public interface AppPersistenceAdapterPort {
    Mono<FranchiseEntity> createFranchise(FranchiseEntity franchiseEntity);
    Mono<BranchEntity> addBranch(BranchEntity branchEntity);
    Mono<ProductEntity> addProduct(ProductEntity productEntity);

    Mono<ProductEntity> findProductById(Long productId);

    Mono<Boolean> existsByProductId(Long productId);
    Mono<Void> deleteProduct(Long productId, Long branchId);

    Mono<ProductEntity> updateProduct(ProductEntity productEntity, Long productId);
    Mono<BranchEntity> getTopProductsPerBranch(Long franchiseId);

    Mono<Boolean> existsFranchiseByName(String name);
    Mono<Boolean> existsInBranchByFranchiseId(Long franchiseId);
    Mono<Boolean> existsByBranchName(String name);
    Mono<Boolean> existsFranchiseByIdExists(Long id);
    Mono<Boolean> existsInProductByBranchId(Long branchId);
    Mono<Boolean> existsByProductName(String name);
    Mono<Boolean> existsBranchesByIdExists(Long branchId);
}
