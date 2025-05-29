package com.network.franchise.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for creating a product")
public class CreateProductResponseDto {
    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "Name of the product", example = "Product Name")
    private String name;

    @NotNull
    @Schema(description = "Product Stock", example = "150")
    private Long stock;

    @NotNull
    @Schema(description = "Branch ID", example = "1")
    private Long branchId;
}
