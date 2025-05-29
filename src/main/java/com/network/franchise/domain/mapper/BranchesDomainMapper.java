package com.network.franchise.domain.mapper;

import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import org.springframework.stereotype.Component;

@Component
public class BranchesDomainMapper {

    public Branch toDomainFromBranchRequestDto(Branch branch, Long franchiseId) {
        if (branch == null || franchiseId == null) return null;
        return Branch.builder()
                .franchiseId(franchiseId)
                .name(branch.getName())
                .build();
    }

    public BranchEntity toEntityFromDomainBranch(Branch domain) {
        if (domain == null) return null;
        return BranchEntity.builder()
                .name(domain.getName())
                .franchiseId(domain.getFranchiseId())
                .build();
    }

    public CreateBranchResponseDto toDomainFromBranchEntity(BranchEntity entity) {
        if (entity == null) return null;
        return CreateBranchResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }

    public Branch toDomainFromBranch(Branch req) {
        if (req == null) return null;
        return Branch.builder()
                .id(req.getId())
                .name(req.getName())
                .franchiseId(req.getFranchiseId())
                .build();
    }
}
