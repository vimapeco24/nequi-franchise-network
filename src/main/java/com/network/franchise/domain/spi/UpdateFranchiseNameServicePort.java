package com.network.franchise.domain.spi;

import com.network.franchise.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface UpdateFranchiseNameServicePort {
    Mono<Long> updateFranchiseName(Franchise request);
}
