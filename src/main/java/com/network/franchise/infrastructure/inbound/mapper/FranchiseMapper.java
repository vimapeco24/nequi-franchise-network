package com.network.franchise.infrastructure.inbound.mapper;

import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.dto.request.CreateFranchiseRequestDto;
import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import org.springframework.stereotype.Component;

import static com.network.franchise.infrastructure.inbound.shield.InputSanitizer.blindStr;

@Component
public class FranchiseMapper {

    public Franchise toDomainFromFranchiseRequestDto(CreateFranchiseRequestDto entity) {
        if (entity == null) return null;
        return Franchise.builder()
                .name(blindStr(entity.getName()))
                .build();
    }

    public FranchiseEntity toEntityFromDomainFranchise(Franchise domain) {
        if (domain == null) return null;
        return FranchiseEntity.builder()
                .id(domain.getId())
                .name(blindStr(domain.getName()))
                .build();
    }

    public CreateFranchiseResponseDto toDomainFromFranchiseEntity(FranchiseEntity entity) {
        if (entity == null) return null;
        return CreateFranchiseResponseDto.builder()
                .id(entity.getId())
                .name(blindStr(entity.getName()))
                .build();
    }

    public Franchise toDomainFromUpdateFranchiseNameRequestDto(Franchise franchise, Long franchiseId) {
        if (franchise == null) return null;
        return Franchise.builder()
                .id(franchiseId)
                .name(blindStr(franchise.getName()))
                .build();
    }
}
