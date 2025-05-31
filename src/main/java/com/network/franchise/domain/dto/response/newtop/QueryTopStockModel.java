package com.network.franchise.domain.dto.response.newtop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryTopStockModel {
    private Long productId;
    private String productName;
    private Long productStock;
    private Long franchiseId;
    private Long branchId;
}
