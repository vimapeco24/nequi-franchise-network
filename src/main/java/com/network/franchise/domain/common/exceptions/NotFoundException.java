package com.network.franchise.domain.common.exceptions;

import com.network.franchise.domain.common.enums.TechnicalMessage;

public class NotFoundException extends RuntimeException {
    public NotFoundException(TechnicalMessage message, String args) {
        super(message.getMessage() + ": " + args);
    }
}
