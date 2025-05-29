package com.network.franchise.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for Updating the stock of a product")
public class ModifiedStockOfProductRequestDto {
    @Schema(description = "Product Stock", example = "100")
    private Long stock;
}
