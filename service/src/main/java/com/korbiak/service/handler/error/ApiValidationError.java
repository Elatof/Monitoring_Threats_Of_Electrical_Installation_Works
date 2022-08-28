package com.korbiak.service.handler.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiValidationError implements ApiSubError {

    private String field;
    private Object rejectedValue;
    private String message;
}
