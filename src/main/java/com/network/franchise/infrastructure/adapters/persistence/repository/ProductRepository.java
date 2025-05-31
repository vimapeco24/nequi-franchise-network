package com.network.franchise.infrastructure.adapters.persistence.repository;

import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Mono<ProductEntity> findTopByBranchIdOrderByStockDesc(Long branchId);
    Flux<ProductEntity> findProductEntityByBranchId(Long franchiseId);
    Mono<Boolean> existsByName(String name);

    @Modifying
    @Query("DELETE FROM public.products WHERE branch_id = :branchId AND id = :productId")
    Mono<Void> deleteProduct(Long branchId, Long productId);
}
