package com.testtask.nauka.common.exceptions;

public class UniqueFieldDuplicationException extends InvalidFieldsException {
    public UniqueFieldDuplicationException(String msg) {
        super(msg);
    }

    public UniqueFieldDuplicationException(String reason, String field) {
        super(reason, field, reason);
    }

    public UniqueFieldDuplicationException(String message, String field, String reason) {
        super(message, field, reason);
    }
}
