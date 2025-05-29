package com.network.franchise.domain.spi;

import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface AddBranchServicePort {
    Mono<CreateBranchResponseDto> addBranch(Branch branch);
}
