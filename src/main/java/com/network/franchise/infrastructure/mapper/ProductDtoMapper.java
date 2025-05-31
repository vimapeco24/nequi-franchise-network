package com.network.franchise.infrastructure.mapper;

import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;

public interface ProductDtoMapper {
    ProductEntity toEntityFromDomainProduct(Product domain);
    CreateProductResponseDto toDomainFromProductEntity(ProductEntity entity);
}
