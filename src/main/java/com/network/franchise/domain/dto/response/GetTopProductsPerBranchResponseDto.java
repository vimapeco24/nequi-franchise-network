package com.network.franchise.domain.dto.response;

import com.network.franchise.domain.dto.response.top.ResponseDtoItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for GetTopProductsPerBranch")
public class GetTopProductsPerBranchResponseDto {
    private List<ResponseDtoItem> responseDto;
}
