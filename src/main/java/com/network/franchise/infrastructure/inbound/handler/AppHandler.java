package com.network.franchise.infrastructure.inbound.handler;

import com.network.franchise.domain.common.ErrorDto;
import com.network.franchise.domain.common.enums.TechnicalMessage;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.common.exceptions.ProcessorException;
import com.network.franchise.domain.dto.request.*;
import com.network.franchise.infrastructure.inbound.mapper.BranchesMapper;
import com.network.franchise.infrastructure.inbound.mapper.FranchiseMapper;
import com.network.franchise.infrastructure.inbound.mapper.ProductsMapper;
import com.network.franchise.domain.model.Branch;
import com.network.franchise.domain.model.Franchise;
import com.network.franchise.domain.model.Product;
import com.network.franchise.domain.spi.*;
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
    public final GetTopProductsPerBranchServicePort getTopProductsPerBranchServicePort;
    public final UpdateFranchiseNameServicePort updateFranchiseNameServicePort;

    private final FranchiseMapper franchiseMapper;
    private final BranchesMapper branchesMapper;
    private final ProductsMapper productsMapper;

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
        return Mono.just(request.pathVariable("branchId"))
                .filter(branchIdStr -> !branchIdStr.isBlank() && branchIdStr.matches("\\d+"))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST)))
                .zipWith(Mono.just(request.pathVariable("productId"))
                        .filter(productIdStr -> !productIdStr.isBlank() && productIdStr.matches("\\d+"))
                        .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST)))
                )
                .flatMap(tuple -> {
                    Long branchId = Long.parseLong(tuple.getT1());
                    Long productId = Long.parseLong(tuple.getT2());
                    return deleteProductServicePort.deleteProduct(branchId, productId)
                            .then(ServerResponse.accepted().build());
                })
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
        return Mono.just(request.pathVariable("productId"))
                .filter(productIdStr -> !productIdStr.isBlank() && productIdStr.matches("\\d+"))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST)))
                .map(Long::parseLong)
                .flatMap(productId ->
                        request.bodyToMono(UpdateProductStockRequestDto.class)
                                .flatMap(dto -> {
                                    Product product = Product.builder()
                                            .id(productId)
                                            .stock(Math.toIntExact(dto.getStock()))
                                            .build();
                                    return updateStockServicePort
                                            .updateStock(productsMapper.toDomainFromUpdateProductRequestDto(product, productId))
                                            .flatMap(res -> ServerResponse.ok().bodyValue(res));
                                })
                )
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
        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));
        return getTopProductsPerBranchServicePort.getTopProductsPerBranch(franchiseId)
                .collectList()
                .flatMap(res -> ServerResponse.ok().bodyValue(res))
                .doOnError(error -> log.error(GET_ERROR, error.getMessage()))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST, ex.getTechnicalMessage(),
                        List.of(ErrorDto.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .parameter(ex.getTechnicalMessage().getParameter())
                                .build())
                ));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));
        return request.bodyToMono(UpdateFranchiseNameRequestDto.class)
                .flatMap(dto -> {
                    Franchise franchise = Franchise.builder()
                            .id(franchiseId)
                            .name(dto.getName())
                            .build();
                    return updateFranchiseNameServicePort.updateFranchiseName(franchiseMapper.toDomainFromUpdateFranchiseNameRequestDto(franchise, franchiseId))
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
}