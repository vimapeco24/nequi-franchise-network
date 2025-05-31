package com.network.franchise.infrastructure.inbound.mapper;

import com.network.franchise.domain.model.Branch;
import org.springframework.stereotype.Component;

@Component
public class BranchesMapper {

    public Branch toDomainFromBranchRequestDto(Branch branch, Long franchiseId) {
        if (branch == null || franchiseId == null) return null;
        return Branch.builder()
                .franchiseId(franchiseId)
                .name(branch.getName())
                .build();
    }
}
