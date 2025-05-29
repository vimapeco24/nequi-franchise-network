package com.network.franchise.infrastructure.inbound.handler;

import com.network.franchise.domain.common.ErrorDto;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.dto.request.CreateBranchRequestDto;
import com.network.franchise.domain.dto.request.CreateProductRequestDto;
import com.network.franchise.domain.dto.request.UpdateProductStockRequestDto;
import com.network.franchise.domain.mapper.BranchesDomainMapper;
import com.network.franchise.domain.mapper.FranchiseDomainMapper;
import com.network.franchise.domain.mapper.ProductsDomainMapper;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.domain.model.Product;
import com.network.franchise.domain.spi.*;
import com.network.franchise.domain.dto.request.CreateFranchiseRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.network.franchise.domain.common.ErrorBuilder.buildErrorResponse;
import static com.network.franchise.domain.common.util.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Tag(name = "Franchises", description = "Franchises Management Services API")
public class AppHandler {

    private final CreateFranchiseServicePort createFranchiseServicePort;
    private final AddBranchServicePort addBranchServicePort;
    private final AddProductServicePort addProductServicePort;
    private final DeleteProductServicePort deleteProductServicePort;
    private final UpdateStockServicePort updateStockServicePort;

    private final FranchiseDomainMapper franchiseMapper;
    private final BranchesDomainMapper branchesMapper;
    private final ProductsDomainMapper productsMapper;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(CreateFranchiseRequestDto.class)
                .flatMap(req -> createFranchiseServicePort
                        .createTechnology(franchiseMapper.toDomainFromFranchiseRequestDto(req)))
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise))
                .doOnError(error -> log.error(CREATE_ERROR, error.getMessage()))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST, ex.getTechnicalMessage(),
                        List.of(ErrorDto.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .parameter(ex.getTechnicalMessage().getParameter())
                                .build())
                ));
    }

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));

        return request.bodyToMono(CreateBranchRequestDto.class)
                .flatMap(dto -> {
                    Branch branch = Branch.builder()
                            .franchiseId(franchiseId)
                            .name(dto.getName())
                            .build();
                    return addBranchServicePort.addBranch(branchesMapper.toDomainFromBranchRequestDto(branch, franchiseId))
                            .flatMap(res -> ServerResponse.ok().bodyValue(res));
                })
                .doOnError(error -> log.error(ADD_ERROR, error.getMessage()))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST, ex.getTechnicalMessage(),
                        List.of(ErrorDto.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .parameter(ex.getTechnicalMessage().getParameter())
                                .build())
                ));
    }

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        Long branchId = Long.parseLong(request.pathVariable("branchId"));

        return request.bodyToMono(CreateProductRequestDto.class)
                .flatMap(dto -> {
                    Product product = Product.builder()
                            .branchId(branchId)
                            .stock(Math.toIntExact(dto.getStock()))
                            .name(dto.getName())
                            .build();
                    return addProductServicePort.addProduct(productsMapper.toDomainFromProductRequestDto(product, branchId))
                            .flatMap(res -> ServerResponse.ok().bodyValue(res));
                })
                .doOnError(error -> log.error(ADD_ERROR, error.getMessage()))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST, ex.getTechnicalMessage(),
                        List.of(ErrorDto.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .parameter(ex.getTechnicalMessage().getParameter())
                                .build())
                ));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long branchId = Long.parseLong(request.pathVariable("branchId"));
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return deleteProductServicePort.deleteProduct(branchId, productId)
                .then(ServerResponse.accepted().build())
                .doOnError(error -> log.error(DELETE_ERROR, error.getMessage()))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST, ex.getTechnicalMessage(),
                        List.of(ErrorDto.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .parameter(ex.getTechnicalMessage().getParameter())
                                .build())
                ));
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return request.bodyToMono(UpdateProductStockRequestDto.class)
                .flatMap(dto -> {
                    Product product = Product.builder()
                            .id(productId)
                            .stock(Math.toIntExact(dto.getStock()))
                            .build();
                    return updateStockServicePort.updateStock(productsMapper.toDomainFromUpdateProductRequestDto(product, productId))
                            .flatMap(res -> ServerResponse.ok().bodyValue(res));
                })
                .doOnError(error -> log.error(UPDATE_ERROR, error.getMessage()))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST, ex.getTechnicalMessage(),
                        List.of(ErrorDto.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .parameter(ex.getTechnicalMessage().getParameter())
                                .build())
                ));
    }

    public Mono<ServerResponse> getTopProductsPerBranch(ServerRequest request) {
        return null;
    }

//    public Mono<ServerResponse> getTopProductsPerBranch(ServerRequest request) {
//        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));
//        return branchRepository.findByFranchiseId(franchiseId)
//                .flatMap(branchEntity -> productRepository.findTopByBranchIdOrderByStockDesc(branchEntity.getId())
//                        .map(productEntity -> Map.of("branch", branchEntity, "product", productEntity)))
//                .collectList()
//                .flatMap(results -> ServerResponse.ok().bodyValue(results));
//    }
}