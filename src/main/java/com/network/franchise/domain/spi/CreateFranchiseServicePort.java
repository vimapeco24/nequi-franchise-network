package com.network.franchise.domain.spi;

import com.network.franchise.domain.model.Franchise;
import com.network.franchise.dto.response.CreateFranchiseResponseDto;
import reactor.core.publisher.Mono;

public interface CreateFranchiseServicePort {
    Mono<CreateFranchiseResponseDto> createTechnology(Franchise request);
}
