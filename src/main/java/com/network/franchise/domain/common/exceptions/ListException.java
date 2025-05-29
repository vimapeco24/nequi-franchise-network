package com.network.franchise.domain.common.exceptions;

import com.network.franchise.domain.common.enums.TechnicalMessage;

public class ListException extends RuntimeException {
    public ListException(TechnicalMessage message) {
        super(message.getMessage());
    }
}
