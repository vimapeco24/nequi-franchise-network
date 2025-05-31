package com.network.franchise.infrastructure.mapper.impl;

import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import com.network.franchise.infrastructure.mapper.FranchiseDtoMapper;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import org.springframework.stereotype.Component;

import static com.network.franchise.infrastructure.inbound.shield.InputSanitizer.blindStr;

@Component
public class FranchiseDtoMapperImpl implements FranchiseDtoMapper {

    @Override
    public FranchiseEntity toEntityFromDomainFranchise(Franchise domain) {
        if (domain == null) return null;
        return FranchiseEntity.builder()
                .id(domain.getId())
                .name(blindStr(domain.getName()))
                .build();
    }

    @Override
    public CreateFranchiseResponseDto toDomainFromFranchiseEntity(FranchiseEntity entity) {
        if (entity == null) return null;
        return CreateFranchiseResponseDto.builder()
                .id(entity.getId())
                .name(blindStr(entity.getName()))
                .build();
    }
}
