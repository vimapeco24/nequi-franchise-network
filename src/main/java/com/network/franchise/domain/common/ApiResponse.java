package com.network.franchise.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ApiResponse {
    private String code;
    private String message;
    private String date;
    private Object data;
    private List<ErrorDto> errors;
}
