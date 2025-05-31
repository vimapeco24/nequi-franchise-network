package com.network.franchise.domain.common.exceptions;

import com.network.franchise.domain.common.enums.TechnicalMessage;

public class NoContentException extends RuntimeException {
    public NoContentException(TechnicalMessage message) {
        super(message.getMessage());
    }
}
