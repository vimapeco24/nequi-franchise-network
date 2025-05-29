package com.network.franchise.domain.spi;

import com.network.franchise.domain.dto.response.GetTopProductsPerBranchResponseDto;
import com.network.franchise.domain.dto.response.top.TopProductPerBranchDto;
import reactor.core.publisher.Mono;

public interface GetTopProductsPerBranchServicePort {
    Mono<TopProductPerBranchDto> getTopProductsPerBranch(Long franchiseId);
}
