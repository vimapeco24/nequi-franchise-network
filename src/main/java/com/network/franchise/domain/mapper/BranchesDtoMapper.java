package com.network.franchise.domain.mapper;

import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.mapper.impl.BranchesDtoMapperImpl;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;

public interface BranchesDtoMapper {

//    BranchesDtoMapper INSTANCE = new BranchesDtoMapperImpl();

    BranchEntity toEntityFromDomainBranch(Branch domain);

    CreateBranchResponseDto toDomainFromBranchEntity(BranchEntity entity);
}
