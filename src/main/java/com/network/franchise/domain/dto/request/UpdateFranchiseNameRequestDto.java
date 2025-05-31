package com.network.franchise.domain.dto.request;

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
@Schema(description = "Request DTO for update a franchise name")
public class UpdateFranchiseNameRequestDto {

    @NotNull
    @Schema(description = "Name of the franchise", example = "Franchise Name")
    private String name;
}
