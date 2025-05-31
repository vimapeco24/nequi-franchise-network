package com.network.franchise.domain.dto.response.top;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTop {
	private int branchId;
	private String name;
	private int id;
	private int stock;
}