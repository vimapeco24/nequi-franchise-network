package com.network.franchise.infrastructure.adapters.persistence;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.NoContentException;
import com.network.franchise.domain.common.exceptions.ProcessorException;
import com.network.franchise.domain.dto.response.newtop.ProductsTopByBranchItem;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.model.Product;
import com.network.franchise.domain.dto.response.newtop.TopProductByBranchByFranchiseResponseDto;
import com.network.franchise.infrastructure.adapters.persistence.entity.BranchEntity;
import com.network.franchise.infrastructure.adapters.persistence.mapper.AppEntityMapper;
import com.network.franchise.infrastructure.adapters.persistence.repository.BranchRepository;
import com.network.franchise.infrastructure.adapters.persistence.repository.FranchiseRepository;
import com.network.franchise.infrastructure.adapters.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppPersistenceAdapter implements AppPersistenceAdapterPort {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final R2dbcEntityTemplate template;
    private final AppEntityMapper mapper;

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchiseRepository.save(mapper.toEntityFromFranchiseDomain(franchise))
                .map(mapper::toDomainFromFranchiseEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error saving technology", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Branch> addBranch(Branch branch) {
        return branchRepository.save(mapper.toEntityFromBranchDomain(branch))
                .map(mapper::toDomainFromBranchEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error adding branch", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Product> addProduct(Product product) {
        return productRepository.save(mapper.toEntityFromProductDomain(product))
                .map(mapper::toDomainFromProductEntity)
                .switchIfEmpty(Mono.error(new ProcessorException("Error adding product", TechnicalMessage.BAD_REQUEST)));
    }

    @Override
    public Mono<Void> deleteProduct(Long productId, Long branchId) {
        return productRepository.deleteProduct(productId, branchId);
    }

    @Override
    public Mono<Product> findProductById(Long productId) {
        return productRepository.findById(productId)
                .map(mapper::toDomainFromProductEntity);
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
    public Mono<Product> updateProduct(Product product, Long productId) {
        return productRepository.findById(productId)
                .flatMap(existingProductEntity -> {
                    product.setId(existingProductEntity.getId());
                    return productRepository.save(mapper.toEntityFromProductDomain(product))
                            .map(mapper::toDomainFromProductEntity);
                });
    }

    @Override
    public Flux<TopProductByBranchByFranchiseResponseDto> getTopProductsPerBranch(Long franchiseId) {
        return franchiseRepository.existsById(franchiseId)
                .flatMapMany(exists -> {
                    if (exists) {
                        return productRepository.findProductTopByFranchiseIdGroupByBranch(franchiseId)
                                .flatMap(productEntity -> branchRepository.findById(productEntity.getBranchId())
                                        .map(branchEntity -> TopProductByBranchByFranchiseResponseDto.builder()
                                                .franchiseId(Math.toIntExact(franchiseId))
                                                .productsTopByBranch(
                                                        List.of(
                                                                ProductsTopByBranchItem.builder()
                                                                        .branchId(Math.toIntExact(branchEntity.getId()))
                                                                        .productId(Math.toIntExact(productEntity.getProductId()))
                                                                        .stock(Math.toIntExact(productEntity.getProductStock()))
                                                                        .productName(productEntity.getProductName())
                                                                        .build()
                                                        )
                                                )
                                                .build()));
                    }
                    return Mono.error(new ProcessorException("Franchise does not exist", TechnicalMessage.BAD_REQUEST));
                });
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
    public Mono<Long> updateFranchiseName(Franchise request) {
        return franchiseRepository.findById(request.getId())
                .flatMap(existingProductEntity -> updateFranchiseNameRepository(request.getId(), request.getName()));
    }

    @Override
    public Mono<Boolean> existsByNameAndBranchId(String name, Long branchId) {
        return productRepository.existsByNameAndBranchId(name, branchId)
                .flatMap(exists -> {
                    if (exists) return Mono.just(true);
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.error(new ProcessorException("Error checking product existence", TechnicalMessage.BAD_REQUEST)));
    }

    public Mono<Long> updateFranchiseNameRepository(Long id, String name) {
        String sql = "UPDATE public.franchises SET name = :name WHERE id = :id";
        return template
                .getDatabaseClient()
                .sql(sql)
                .bind("name", name)
                .bind("id", id)
                .fetch()
                .rowsUpdated();
    }
}
