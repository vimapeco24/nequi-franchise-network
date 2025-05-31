package com.network.franchise.domain.spi;

import reactor.core.publisher.Mono;

public interface DeleteProductServicePort {
    Mono<Void> deleteProduct(Long branchId, Long productId);
}
