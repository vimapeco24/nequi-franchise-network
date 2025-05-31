package com.network.franchise.infrastructure.mapper;

import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;

public interface FranchiseDtoMapper {
    FranchiseEntity toEntityFromDomainFranchise(Franchise domain);
    CreateFranchiseResponseDto toDomainFromFranchiseEntity(FranchiseEntity entity);
}
