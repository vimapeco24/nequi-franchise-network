package com.network.franchise.domain.dto.response.newtop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for Top Product By Branch By Franchise item")
public class ProductsTopByBranchItem{
	private int branchId;
	private int productId;
	//private String branchName;
	private int stock;
	private String productName;
}