package com.network.franchise.domain.common.exceptions;

import com.network.franchise.domain.common.enums.TechnicalMessage;

public class DuplicateException extends RuntimeException {
    public DuplicateException(TechnicalMessage message) {
        super(message.getMessage());
    }
}
