package com.network.franchise.domain.common;

import com.network.franchise.domain.common.enums.TechnicalMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

public class ErrorBuilder {

    public static Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus,
                                                          TechnicalMessage error,
                                                          List<ErrorDto> errors) {
        return Mono.defer(() -> {
            ApiResponse response = ApiResponse.builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .date(Instant.now().toString())
                    .errors(errors)
                    .build();

            return ServerResponse.status(httpStatus)
                    .body(Mono.just(response), ApiResponse.class);
        });
    }
}
