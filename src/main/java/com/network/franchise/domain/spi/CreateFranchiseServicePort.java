package com.network.franchise.domain.spi;

import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import reactor.core.publisher.Mono;

public interface CreateFranchiseServicePort {
    Mono<Franchise> createTechnology(Franchise request);
}
