package com.network.franchise.infrastructure.mapper;

import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;

public interface BranchesDtoMapper {
    BranchEntity toEntityFromDomainBranch(Branch domain);
    CreateBranchResponseDto toDomainFromBranchEntity(BranchEntity entity);
}
