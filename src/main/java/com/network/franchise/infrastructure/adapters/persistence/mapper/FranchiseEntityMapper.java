package com.network.franchise.infrastructure.adapters.persistence.mapper;

import com.network.franchise.domain.model.Franchise;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import org.springframework.stereotype.Component;

@Component
public class FranchiseEntityMapper {

    public FranchiseEntity toEntityFromFranchiseDomain(Franchise franchise) {
        return FranchiseEntity.builder()
                .name(franchise.getName())
                .build();
    }

    public Franchise toDomainFromFranchiseEntity(FranchiseEntity franchiseEntity) {
        return Franchise.builder()
                .id(franchiseEntity.getId())
                .name(franchiseEntity.getName())
                .build();
    }
}
