package com.network.franchise.domain.mapper;

import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.domain.mapper.impl.ProductDtoMapperImpl;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;

public interface ProductDtoMapper {

//    public ProductDtoMapper INSTANCE = new ProductDtoMapperImpl();

    ProductEntity toEntityFromDomainProduct(Product domain);

    CreateProductResponseDto toDomainFromProductEntity(ProductEntity entity);
}
