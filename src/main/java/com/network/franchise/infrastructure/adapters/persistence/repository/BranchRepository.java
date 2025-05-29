package com.network.franchise.infrastructure.adapters.persistence.repository;

import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
    Flux<BranchEntity> findBranchEntityByFranchiseId(Long franchiseId);
    Mono<Boolean> existsByName(String name);
    Mono<Boolean> existsByNameAndFranchiseId(String name, Long franchiseId);

    Mono<Boolean> existsByFranchiseId(Long franchiseId);
}
