package com.network.franchise.infrastructure.adapters.persistence.repository;

import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {
    Mono<Boolean> existsByName(String name);

    Mono<Boolean> existsByIdExists(Long id);
}
