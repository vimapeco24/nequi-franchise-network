package com.network.franchise.infrastructure.inbound.handler;

import com.network.franchise.infrastructure.adapters.persistence.AppPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestHandler {//implements CommandLineRunner {

//    private final AppPersistenceAdapter repository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        getTopProducts(null)
//                .subscribe(products -> log.info("Top products: {}", products));
//
//    }
//
//    public Mono<ServerResponse> getTopProducts(ServerRequest request) {
//        return repository.getTopProductsByFranchise(1L)
//                .doOnNext(product -> log.info("Top product: {}", product))  // Aquí sí ves el contenido
//                .collectList()
//                .flatMap(products -> ServerResponse.ok().bodyValue(products))
//                .doOnError(e -> log.error("Error fetching top products", e));
//    }
}
