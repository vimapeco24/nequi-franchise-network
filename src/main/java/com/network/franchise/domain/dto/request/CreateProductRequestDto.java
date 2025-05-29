package com.network.franchise.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Create product request")
public class CreateProductRequestDto {
    @Schema(description = "Product name", example = "Product A")
    private String name;

    @Schema(description = "Product description", example = "Description of Product A")
    private Long stock;
}
