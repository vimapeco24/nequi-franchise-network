package com.network.franchise.domain.spi;

import com.network.franchise.domain.dto.response.newtop.TopProductByBranchByFranchiseResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetTopProductsPerBranchServicePort {
    Flux<TopProductByBranchByFranchiseResponseDto> getTopProductsPerBranch(Long franchiseId);
}
