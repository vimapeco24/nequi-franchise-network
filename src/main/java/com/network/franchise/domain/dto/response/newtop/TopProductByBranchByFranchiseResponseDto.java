package com.network.franchise.domain.dto.response.newtop;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for Top Product By Branch By Franchise")
public class TopProductByBranchByFranchiseResponseDto {
	private int franchiseId;
	private List<ProductsTopByBranchItem> productsTopByBranch;
	//private String franchiseName;
}