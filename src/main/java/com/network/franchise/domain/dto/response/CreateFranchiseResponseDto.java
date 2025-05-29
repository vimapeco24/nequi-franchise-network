package com.network.franchise.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for creating a franchise")
public class CreateFranchiseResponseDto {
	@Schema(description = "Unique identifier of the franchise", example = "1")
	private Long id;

	@Schema(description = "Name of the franchise", example = "Franchise Name")
	private String name;
}