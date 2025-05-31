package com.network.franchise.domain.spi;

import com.network.franchise.domain.model.Product;
import reactor.core.publisher.Mono;

public interface UpdateStockServicePort {
    Mono<Product> updateStock(Product product);
}
