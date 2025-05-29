package com.network.franchise.domain.common;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class GlobalErrorAttributes implements ErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        return Map.of(
                "status", 500,
                "error", "Internal Server Error",
                "message", error != null ? error.getMessage() : "Unknown error",
                "path", request.path(),
                "timestamp", LocalDateTime.now()
        );
    }

    @Override
    public Throwable getError(ServerRequest request) {
        return (Throwable) request.attribute("org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR")
                .orElseThrow(() -> new IllegalStateException("No error attribute found"));
    }

    @Override
    public void storeErrorInformation(Throwable error, ServerWebExchange exchange) {
        exchange.getAttributes().put("org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR", error);
    }
}
