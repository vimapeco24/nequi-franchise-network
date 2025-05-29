package com.network.franchise.domain.dto.response.top;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Branch{
	private String name;
	private int franchiseId;
	private int id;
}