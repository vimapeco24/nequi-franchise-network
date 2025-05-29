package com.network.franchise.infrastructure.adapters.persistence;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
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
public class AppPersistenceAdapter implements AppPersistenceAdapterPort {

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
    public Mono<BranchEntity> addBranch(BranchEntity branchEntity) {
        return branchRepository.save(branchEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error adding branch", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<ProductEntity> addProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error adding product", TechnicalMessage.BAD_REQUEST)));
//        return branchRepository.findById(branchId)
//                .flatMap(branchEntity -> {
//                    productEntity.setBranchId(branchEntity.getId());
//                    return productRepository.save(productEntity)
//                            .doOnSuccess(savedProductEntity -> log.info("Product added: {}", savedProductEntity))
//                            .doOnError(error -> log.error("Error adding product: {}", error.getMessage()));
//                })
//                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")));
    }

    @Override
    public Mono<ProductEntity> findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Mono<Boolean> existsByProductId(Long productId) {
        return productRepository.existsById(productId)
                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking product existence", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Void> deleteProduct(Long productId, Long branchId) {
        return productRepository.findById(productId)
                .flatMap(productEntity -> {
                    if (productEntity.getBranchId().equals(branchId)) {
                        return productRepository.delete(productEntity)
                                .doOnSuccess(aVoid -> log.info("Product deleted: {}", productEntity))
                                .doOnError(error -> log.error("Error deleting product: {}", error.getMessage()));
//                                .then(Mono.just(productEntity));
//                                .doOnSuccess(deletedProductEntity -> log.info("Product deleted: {}", deletedProductEntity))
//                                .doOnError(error -> log.error("Error deleting product: {}", error.getMessage()));
                    } else {
                        return Mono.error(new ProcessorException("Product does not belong to this branch", TechnicalMessage.BAD_REQUEST));
                    }
                });
//                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));

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
    public Mono<Boolean> existsFranchiseByName(String name) {
        return franchiseRepository.existsByName(name)
                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking franchise existence", TechnicalMessage.BAD_REQUEST)));
    }

//    @Override
//    public Mono<Boolean> existsInBranchByFranchiseId(Long franchiseId) {
//        return branchRepository.existsByFranchiseId(franchiseId)
//                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
//                .flatMap(exists -> {
//                    if (exists) return Mono.just(true);
//                    return Mono.just(false);
//                })
//                .switchIfEmpty(Mono.error(new ProcessorException("Error checking branch existence", TechnicalMessage.BAD_REQUEST)));
//    }

    @Override
    public Mono<Boolean> existsInBranchByFranchiseId(Long franchiseId) {
        return branchRepository.findBranchEntityByFranchiseId(franchiseId)
                .hasElements()
                .flatMap(exists -> Mono.just(true))
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking branch existence", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Boolean> existsByBranchName(String name) {
        return branchRepository.existsByName(name)
                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking branch existence", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Boolean> existsFranchiseByIdExists(Long id) {
        return franchiseRepository.existsById(id)
                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking franchise existence", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Boolean> existsInProductByBranchId(Long branchId) {
        return productRepository.findProductEntityByBranchId(branchId)
                .hasElements()
                .flatMap(exists -> Mono.just(true))
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking branch existence", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Boolean> existsByProductName(String name) {
        return productRepository.existsByName(name)
                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking product existence", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Boolean> existsBranchesByIdExists(Long id) {
        return branchRepository.existsById(id)
                .switchIfEmpty(Mono.error(new NoContentException(TechnicalMessage.NO_CONTENT)))
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking branch existence", TechnicalMessage.BAD_REQUEST)));
    }


//    @Override
//    public Mono<BranchEntity> findByFranchiseId(Long franchiseId) {
//        return branchRepository.findById(franchiseId)
//                .flatMap(branchEntity -> {
//                    if (branchEntity.getFranchiseId().equals(franchiseId)) return Mono.just(branchEntity);
//                    return Mono.error(new ProcessorException("Branch does not belong to this franchise", TechnicalMessage.BAD_REQUEST));
//                })
//                .switchIfEmpty(Mono.error(new ProcessorException("Error finding branch by franchise ID", TechnicalMessage.BAD_REQUEST)));
//    }//existsById
}
