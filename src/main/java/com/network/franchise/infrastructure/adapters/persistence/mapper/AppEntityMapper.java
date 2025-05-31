package com.network.franchise.infrastructure.adapters.persistence.mapper;

import com.network.franchise.domain.model.Branch;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.model.Product;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class AppEntityMapper {

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

    public Branch toDomainFromBranchEntity(BranchEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }

    public BranchEntity toEntityFromBranchDomain(Branch branch) {
        return BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }

    public ProductEntity toEntityFromProductDomain(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }

    public Product toDomainFromProductEntity(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .stock(productEntity.getStock())
                .branchId(productEntity.getBranchId())
                .build();
    }
}
