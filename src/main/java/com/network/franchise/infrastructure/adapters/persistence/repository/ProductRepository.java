package com.network.franchise.infrastructure.adapters.persistence.repository;

import com.network.franchise.domain.dto.response.newtop.QueryTopStockModel;
import com.network.franchise.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Flux<ProductEntity> findProductEntityByBranchId(Long franchiseId);
    Mono<Boolean> existsByName(String name);
    Mono<Boolean> existsByNameAndBranchId(String name, Long branchId);

    @Modifying
    @Query("DELETE FROM public.products WHERE branch_id = :branchId AND id = :productId")
    Mono<Void> deleteProduct(Long branchId, Long productId);

    @Query("""
        SELECT p.id as product_id, p.name as product_name, p.stock as product_stock, franchise_id, p.branch_id
        FROM products p
        JOIN (
            SELECT branch_id, MAX(stock) AS max_stock
            FROM products
            GROUP BY branch_id
        ) max_products ON p.branch_id = max_products.branch_id AND p.stock = max_products.max_stock
        JOIN branches b ON p.branch_id = b.id
        WHERE b.franchise_id = :franchiseId
    """)
    Flux<QueryTopStockModel> findProductTopByFranchiseIdGroupByBranch(Long franchiseId);
}
