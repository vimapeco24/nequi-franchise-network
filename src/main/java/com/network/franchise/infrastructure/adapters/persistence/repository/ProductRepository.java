package com.network.franchise.infrastructure.adapters.persistence.repository;

import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Flux<ProductEntity> findByBranchId(Long branchId);
    Mono<Void> deleteByBranchIdAndId(Long branchId, Long id);
    Mono<ProductEntity> findTopByBranchIdOrderByStockDesc(Long branchId);
}
