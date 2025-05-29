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
@Schema(description = "Response DTO for creating a branch")
public class CreateBranchResponseDto {
    @Schema(description = "Unique identifier of the branch", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "Name of the branch", example = "Branch Name")
    private String name;

    @NotNull
    @Schema(description = "Branch ID", example = "1")
    private Long franchiseId;
}
