package com.network.franchise.infrastructure.inbound.mapper;

import com.network.franchise.domain.model.Product;
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

    public Product toDomainFromUpdateProductRequestDto(Product product, Long productId) {
        if (product == null || productId == null) return null;
        return Product.builder()
                .id(productId)
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}
