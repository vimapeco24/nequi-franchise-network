package com.network.franchise.infrastructure.adapters.persistence;

import com.network.franchise.domain.api.FranchisePersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.NoContentException;
import com.network.franchise.domain.common.exceptions.ProcessorException;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import com.network.franchise.infrastructure.adapters.persistence.repository.BranchRepository;
import com.network.franchise.infrastructure.adapters.persistence.repository.FranchiseRepository;
import com.network.franchise.infrastructure.adapters.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchisePersistenceAdapter implements FranchisePersistenceAdapterPort {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public Mono<FranchiseEntity> createFranchise(FranchiseEntity franchiseEntity) {
        return franchiseRepository.save(franchiseEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error saving technology", TechnicalMessage.BAD_REQUEST)));
    }

    // TODO: Refactorizations
    @Override
    public Mono<BranchEntity> addBranch(BranchEntity branchEntity, Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchiseEntity -> {
                    branchEntity.setFranchiseId(franchiseEntity.getId());
                    return branchRepository.save(branchEntity)
                            .doOnSuccess(savedBranchEntity -> log.info("Branch added: {}", savedBranchEntity))
                            .doOnError(error -> log.error("Error adding branch: {}", error.getMessage()));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")));

//        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));
//        return request.bodyToMono(Branch.class)
//                .map(branch -> branch.toBuilder().franchiseId(franchiseId).build())
//                .flatMap(branchRepository::save)
//                .flatMap(branch -> ServerResponse.ok().bodyValue(branch));
    }

    @Override
    public Mono<ProductEntity> addProduct(ProductEntity productEntity, Long branchId) {
        return branchRepository.findById(branchId)
                .flatMap(branchEntity -> {
                    productEntity.setBranchId(branchEntity.getId());
                    return productRepository.save(productEntity)
                            .doOnSuccess(savedProductEntity -> log.info("Product added: {}", savedProductEntity))
                            .doOnError(error -> log.error("Error adding product: {}", error.getMessage()));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")));

//        Long branchId = Long.parseLong(request.pathVariable("branchId"));
//        return request.bodyToMono(Product.class)
//                .map(product -> product.toBuilder().branchId(branchId).build())
//                .flatMap(productRepository::save)
//                .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }

    @Override
    public Mono<ProductEntity> deleteProduct(Long productId, Long branchId) {
        return productRepository.findById(productId)
                .flatMap(productEntity -> {
                    if (productEntity.getBranchId().equals(branchId)) {
                        return productRepository.delete(productEntity)
                                .then(Mono.just(productEntity))
                                .doOnSuccess(deletedProductEntity -> log.info("Product deleted: {}", deletedProductEntity))
                                .doOnError(error -> log.error("Error deleting product: {}", error.getMessage()));
                    } else {
                        return Mono.error(new RuntimeException("Product does not belong to this branch"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));

//        Long branchId = Long.parseLong(request.pathVariable("branchId"));
//        Long productId = Long.parseLong(request.pathVariable("productId"));
//        return productRepository.deleteByBranchIdAndId(branchId, productId)
//                .then(ServerResponse.noContent().build());
    }

    @Override
    public Mono<ProductEntity> updateProduct(ProductEntity productEntity, Long productId) {
        return productRepository.findById(productId)
                .flatMap(existingProductEntity -> {
                    productEntity.setId(existingProductEntity.getId());
                    return productRepository.save(productEntity)
                            .doOnSuccess(updatedProductEntity -> log.info("Product updated: {}", updatedProductEntity))
                            .doOnError(error -> log.error("Error updating product: {}", error.getMessage()));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));

//        Long productId = Long.parseLong(request.pathVariable("productId"));
//        return productRepository.findById(productId)
//                .zipWith(request.bodyToMono(Product.class))
//                .map(tuple -> {
//                    Product existing = tuple.getT1();
//                    int newStock = tuple.getT2().getStock();
//                    existing.setStock(newStock);
//                    return existing;
//                })
//                .flatMap(productRepository::save)
//                .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }

    @Override
    public Mono<BranchEntity> getTopProductsPerBranch(Long franchiseId) {
        return null;


//        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));
//        return branchRepository.findByFranchiseId(franchiseId)
//                .flatMap(branch -> productRepository.findTopByBranchIdOrderByStockDesc(branch.getId())
//                        .map(product -> Map.of("branch", branch, "product", product)))
//                .collectList()
//                .flatMap(results -> ServerResponse.ok().bodyValue(results));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return franchiseRepository.existsByName(name)
                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking franchise existence", TechnicalMessage.BAD_REQUEST)));
    }
}
