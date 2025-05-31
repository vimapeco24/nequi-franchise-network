package com.network.franchise.domain.mapper.impl;

import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.mapper.BranchesDtoMapper;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import org.springframework.stereotype.Component;

@Component
public class BranchesDtoMapperImpl implements BranchesDtoMapper {

    @Override
    public BranchEntity toEntityFromDomainBranch(Branch domain) {
        if (domain == null) return null;
        return BranchEntity.builder()
                .name(domain.getName())
                .franchiseId(domain.getFranchiseId())
                .build();
    }

    @Override
    public CreateBranchResponseDto toDomainFromBranchEntity(BranchEntity entity) {
        if (entity == null) return null;
        return CreateBranchResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }
}
