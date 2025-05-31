package com.network.franchise.domain.mapper;

import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import com.network.franchise.domain.mapper.impl.FranchiseDtoMapperImpl;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;

public interface FranchiseDtoMapper {

//    public FranchiseDtoMapper INSTANCE = new FranchiseDtoMapperImpl();

    FranchiseEntity toEntityFromDomainFranchise(Franchise domain);

    CreateFranchiseResponseDto toDomainFromFranchiseEntity(FranchiseEntity entity);
}
