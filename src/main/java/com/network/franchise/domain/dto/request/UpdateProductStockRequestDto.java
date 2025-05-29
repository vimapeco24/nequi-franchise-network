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
@Schema(description = "Request DTO for updating product stock")
public class UpdateProductStockRequestDto {

    @Schema(description = "Stock quantity", example = "100")
    private Long stock;
}
