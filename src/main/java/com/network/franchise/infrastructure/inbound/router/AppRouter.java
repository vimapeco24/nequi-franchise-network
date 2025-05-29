package com.network.franchise.infrastructure.inbound.router;

import com.network.franchise.domain.common.ErrorDto;
import com.network.franchise.domain.dto.request.CreateBranchRequestDto;
import com.network.franchise.domain.dto.request.CreateFranchiseRequestDto;
import com.network.franchise.domain.dto.request.CreateProductRequestDto;
import com.network.franchise.domain.dto.response.CreateBranchResponseDto;
import com.network.franchise.domain.dto.response.CreateFranchiseResponseDto;
import com.network.franchise.domain.dto.response.CreateProductResponseDto;
import com.network.franchise.infrastructure.inbound.handler.AppHandler;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AppRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/franchises",
                    produces = "application/json",
                    method = RequestMethod.POST,
                    beanClass = AppHandler.class,
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
            @RouterOperation(
                    path = "/api/v1/franchises/{franchiseId}/branches",
                    produces = "application/json",
                    method = RequestMethod.POST,
                    beanClass = AppHandler.class,
                    beanMethod = "addBranch",
                    operation = @io.swagger.v3.oas.annotations.Operation(
                            operationId = "addBranch",
                            summary = "Add a new Branch to a Franchise",
                            description = "Add a new Branch to an existing Franchise in the database.",
                            parameters = {
                                    @Parameter(name = "franchiseId", in = ParameterIn.PATH, description = "Franchise ID", example = "1"),
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Branch Request DTO",
                                    content = @io.swagger.v3.oas.annotations.media.Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CreateBranchRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "201",
                                            description = "Created",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CreateBranchResponseDto.class)
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
            @RouterOperation(
                    path = "/api/v1/branches/{branchId}/products",
                    produces = "application/json",
                    method = RequestMethod.POST,
                    beanClass = AppHandler.class,
                    beanMethod = "addProduct",
                    operation = @io.swagger.v3.oas.annotations.Operation(
                            operationId = "addProduct",
                            summary = "Add a new Product to a Branch",
                            description = "Add a new Product to an existing Branch in the database.",
                            parameters = {
                                    @Parameter(name = "branchId", in = ParameterIn.PATH, description = "Branch ID", example = "1"),
                            },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Product Request DTO",
                                    content = @io.swagger.v3.oas.annotations.media.Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CreateProductRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "201",
                                            description = "Created",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CreateProductResponseDto.class)
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
                                    ),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "404",
                                            description = "Not Found",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDto.class)
                                            )
                                    )
                            }
                    )
            ),
    })
    public RouterFunction<ServerResponse> franchiseRoutes(AppHandler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/franchises", handler::createFranchise)
                .POST("/api/v1/franchises/{franchiseId}/branches", handler::addBranch)
                .POST("/api/v1/branches/{branchId}/products", handler::addProduct)
//                .DELETE("/branches/{branchId}/products/{productId}", handler::deleteProduct)
//                .PUT("/products/{productId}/stock", handler::updateStock)
//                .GET("/franchises/{franchiseId}/top-products", handler::getTopProductsPerBranch)
                .build();
    }
}

