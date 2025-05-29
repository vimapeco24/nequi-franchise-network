package com.network.franchise.infrastructure.inbound.router;

import com.network.franchise.domain.common.ErrorDto;
import com.network.franchise.domain.dto.request.CreateFranchiseRequestDto;
import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import com.network.franchise.infrastructure.inbound.handler.FranchiseHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FranchiseRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/franchises",
                    produces = "application/json",
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "createFranchise",
                    operation = @io.swagger.v3.oas.annotations.Operation(
                            operationId = "createFranchise",
                            summary = "Create a new Franchise",
                            description = "Create a new Franchise in the database.",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Franchise Request DTO",
                                    content = @io.swagger.v3.oas.annotations.media.Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CreateFranchiseRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "201",
                                            description = "Created",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CreateFranchiseResponseDto.class)
                                            )
                                    ),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "400",
                                            description = "Bad Request",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDto.class)
                                            )
                                    ),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "409",
                                            description = "Conflict",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDto.class)
                                            )
                                    ),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "500",
                                            description = "Internal Server Error",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDto.class)
                                            )
                                    )
                            }
                    )
            ),
    })
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/franchises", handler::createFranchise)
                .POST("/franchises/{franchiseId}/branches", handler::addBranch)
                .POST("/branches/{branchId}/products", handler::addProduct)
                .DELETE("/branches/{branchId}/products/{productId}", handler::deleteProduct)
                .PUT("/products/{productId}/stock", handler::updateStock)
                .GET("/franchises/{franchiseId}/top-products", handler::getTopProductsPerBranch)
                .build();
    }
}

