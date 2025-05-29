package com.network.franchise.domain.common.exceptions;

import com.network.franchise.domain.common.enums.TechnicalMessage;

public class MissingException extends RuntimeException {
    public MissingException(TechnicalMessage message, String value) {
        super(message.getMessage() + ": " + value);
    }
}
