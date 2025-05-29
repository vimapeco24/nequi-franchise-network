package com.network.franchise.infrastructure.adapters.persistence.repository;

import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);
}
