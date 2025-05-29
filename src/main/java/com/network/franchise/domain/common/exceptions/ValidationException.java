package com.network.franchise.domain.common.exceptions;

import com.network.franchise.domain.common.enums.TechnicalMessage;

public class ValidationException extends RuntimeException {
    public ValidationException(TechnicalMessage message) {
        super(message.getMessage());
    }
}
