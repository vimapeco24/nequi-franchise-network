package com.network.franchise.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error DTO", title = "Error DTO")
public class ErrorDto {
    @Schema(description = "Error code")
    private String code;

    @Schema(description = "Error type")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String parameter;

    @Schema(description = "Error message")
    private String message;
}
