package com.network.franchise.infrastructure.adapters.persistence;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.NoContentException;
import com.network.franchise.domain.common.exceptions.ProcessorException;
import com.network.franchise.domain.dto.response.top.Branch;
import com.network.franchise.domain.dto.response.top.Product;
import com.network.franchise.domain.dto.response.top.TopProductPerBranchDto;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.FranchiseEntity;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import com.network.franchise.infrastructure.adapters.persistence.repository.BranchRepository;
import com.network.franchise.infrastructure.adapters.persistence.repository.FranchiseRepository;
import com.network.franchise.infrastructure.adapters.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppPersistenceAdapter implements AppPersistenceAdapterPort {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<FranchiseEntity> createFranchise(FranchiseEntity franchiseEntity) {
        return franchiseRepository.save(franchiseEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error saving technology", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<BranchEntity> addBranch(BranchEntity branchEntity) {
        return branchRepository.save(branchEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error adding branch", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<ProductEntity> addProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error adding product", TechnicalMessage.BAD_REQUEST)));
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
        return productRepository.deleteProduct(productId, branchId);
    }

    @Override
    public Mono<ProductEntity> updateProduct(ProductEntity productEntity, Long productId) {
        return productRepository.findById(productId)
                .flatMap(existingProductEntity -> {
                    productEntity.setId(existingProductEntity.getId());
                    return productRepository.save(productEntity);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error updating product", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<TopProductPerBranchDto> getTopProductsPerBranch(Long franchiseId) {
        return findByFranchiseId(franchiseId)
                .flatMap(branch -> productRepository.findTopByBranchIdOrderByStockDesc(branch.getId())
                                .map(productEntity -> TopProductPerBranchDto.builder()
                                        .branch(Branch.builder()
                                                .franchiseId(Math.toIntExact(branch.getId()))
                                                .name(branch.getName())
                                                .id(Math.toIntExact(branch.getId()))
                                                .build())
                                        .product(Product.builder()
                                                .stock(productEntity.getStock())
                                                .name(productEntity.getName())
                                                .id(Math.toIntExact(productEntity.getId()))
                                                .branchId(Math.toIntExact(productEntity.getBranchId()))
                                                .build())
                                        .build()));
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


    @Override
    public Mono<BranchEntity> findByFranchiseId(Long franchiseId) {
        return branchRepository.findById(franchiseId)
                .flatMap(branchEntity -> {
                    if (branchEntity.getFranchiseId().equals(franchiseId)) return Mono.just(branchEntity);
                    return Mono.error(new ProcessorException("Branch does not belong to this franchise", TechnicalMessage.BAD_REQUEST));
                });
    }

    @Override
    public Mono<Long> updateFranchiseName(FranchiseEntity request) {
        return franchiseRepository.findById(request.getId())
                .flatMap(existingProductEntity -> updateFranchiseNameRepository(request.getId(), request.getName()));
    }

    public Mono<Long> updateFranchiseNameRepository(Long id, String name) {
        String sql = "UPDATE public.franchises SET name = :name, updated_at = CURRENT_TIMESTAMP WHERE id = :id";
        return template
                .getDatabaseClient()
                .sql(sql)
                .bind("name", name)
                .bind("id", id)
                .fetch()
                .rowsUpdated();
    }
}
