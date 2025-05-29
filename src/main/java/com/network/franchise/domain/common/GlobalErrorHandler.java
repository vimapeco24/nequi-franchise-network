package com.network.franchise.domain.common;

import com.network.franchise.domain.common.exceptions.*;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Order(-2)
public class GlobalErrorHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorHandler(GlobalErrorAttributes errorAttributes,
                              ApplicationContext applicationContext,
                              ServerCodecConfigurer serverCodecConfigurer,
                              WebProperties webProperties) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);

        int status;
        String errorMessage = error.getMessage();
        String errorType;

        switch (error) {
            case NoContentException ignored2 -> {
                status = 204;
                errorType = "No Content";
            }
            case NotFoundException ignored2 -> {
                status = 404;
                errorType = "Not Found";
            }
            case ValidationException ignored1 -> {
                status = 400;
                errorType = "Bad Request";
            }
            case DuplicateException ignored -> {
                status = 409;
                errorType = "Duplicate Entry";
            }
            case ListException ignored -> {
                status = 400;
                errorType = "Bad Request";
            }
            case MissingException ignored -> {
                status = 400;
                errorType = "Missing Parameter";
            }
            case BusinessException ignored3 -> {
                status = 500;
                errorType = "Business Error";
            }
            case ProcessorException ignored -> {
                status = 400;
                errorType = "Processor Error";
            }
            default -> {
                status = 500;
                errorType = "Internal Server Error";
                log.error(error.getMessage(), error);
            }
        }

        List<ErrorDto> errors = List.of(ErrorDto.builder()
                .code(errorType)
                .message(errorMessage)
                .parameter(request.path())
                .build()
        );

        Map<String, Object> response = Map.of(
                "status", status,
                "error", errors,
                "path", request.path(),
                "timestamp", java.time.OffsetDateTime.now()
        );

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response)
                .doOnError(e -> log.error("Error occurred: {}", e.getMessage()));
    }
}
