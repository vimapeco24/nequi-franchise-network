package com.network.franchise.infrastructure.inbound.handler;

import com.network.franchise.domain.common.ErrorDto;
import com.network.franchise.domain.common.exceptions.BusinessException;
import com.network.franchise.domain.mapper.FranchiseDomainMapper;
import com.network.franchise.domain.spi.CreateFranchiseServicePort;
import com.network.franchise.dto.request.CreateFranchiseRequestDto;
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
import static com.network.franchise.domain.common.util.Constants.CREATE_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
@Tag(name = "Franchise", description = "Franchise Management Services API")
public class FranchiseHandler {

    private final CreateFranchiseServicePort createFranchiseServicePort;
    private final FranchiseDomainMapper mapper;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(CreateFranchiseRequestDto.class)
                .flatMap(req -> createFranchiseServicePort.createTechnology(mapper.toDomainFromFranchiseRequestDto(req)))
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


}