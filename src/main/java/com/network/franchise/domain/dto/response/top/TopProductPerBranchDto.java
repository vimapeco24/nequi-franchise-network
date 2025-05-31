package com.network.franchise.domain.dto.response.top;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopProductPerBranchDto {
    private BranchTop branchTop;
    private ProductTop productTop;
}
