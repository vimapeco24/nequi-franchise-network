package com.network.franchise.domain.mapper.impl;

import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.domain.mapper.ProductDtoMapper;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapperImpl implements ProductDtoMapper {

    @Override
    public ProductEntity toEntityFromDomainProduct(Product domain) {
        if (domain == null) return null;
        return ProductEntity.builder()
                .name(domain.getName())
                .branchId(domain.getBranchId())
                .stock(domain.getStock())
                .build();
    }

    @Override
    public CreateProductResponseDto toDomainFromProductEntity(ProductEntity entity) {
        if (entity == null) return null;
        return CreateProductResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock((long) entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }
}
