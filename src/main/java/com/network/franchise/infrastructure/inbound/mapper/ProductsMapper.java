package com.network.franchise.infrastructure.inbound.mapper;

import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductsMapper {

    public Product toDomainFromProductRequestDto(Product product, Long branchId) {
        if (product == null || branchId == null) return null;
        return Product.builder()
                .branchId(branchId)
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }

    public ProductEntity toEntityFromDomainProduct(Product domain) {
        if (domain == null) return null;
        return ProductEntity.builder()
                .name(domain.getName())
                .branchId(domain.getBranchId())
                .stock(domain.getStock())
                .build();
    }

    public CreateProductResponseDto toDomainFromProductEntity(ProductEntity entity) {
        if (entity == null) return null;
        return CreateProductResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock((long) entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }

    public Product toDomainFromUpdateProductRequestDto(Product product, Long productId) {
        if (product == null || productId == null) return null;
        return Product.builder()
                .id(productId)
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}
