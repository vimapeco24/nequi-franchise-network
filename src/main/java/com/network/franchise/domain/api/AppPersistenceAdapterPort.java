package com.network.franchise.domain.api;

import com.network.franchise.domain.dto.response.newtop.TopProductByBranchByFranchiseResponseDto;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppPersistenceAdapterPort {
    Mono<Franchise> createFranchise(Franchise franchise);
    Mono<Branch> addBranch(Branch branchEntity);
    Mono<Product> addProduct(Product product);
    Mono<Void> deleteProduct(Long productId, Long branchId);
    Mono<Product> updateProduct(Product product, Long productId);
    Flux<TopProductByBranchByFranchiseResponseDto> getTopProductsPerBranch(Long franchiseId);
    Mono<Long> updateFranchiseName(Franchise request);

    Mono<Product> findProductById(Long productId);
    Mono<Boolean> existsByProductId(Long productId);
    Mono<Boolean> existsFranchiseByName(String name);
    Mono<Boolean> existsInBranchByFranchiseId(Long franchiseId);
    Mono<Boolean> existsByBranchName(String name);
    Mono<Boolean> existsFranchiseByIdExists(Long id);
    Mono<Boolean> existsInProductByBranchId(Long branchId);
    Mono<Boolean> existsByProductName(String name);
    Mono<Boolean> existsBranchesByIdExists(Long branchId);
    Mono<BranchEntity> findByFranchiseId(Long franchiseId);
    Mono<Boolean> existsByNameAndBranchId(String name, Long branchId);
}
