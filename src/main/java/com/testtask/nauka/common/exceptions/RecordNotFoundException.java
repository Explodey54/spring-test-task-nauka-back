package com.testtask.nauka.common.exceptions;

public class RecordNotFoundException extends InvalidFieldsException {
    public RecordNotFoundException(String msg) {
        super(msg);
    }

    public RecordNotFoundException(String reason, String field) {
        super(reason, field, reason);
    }

    public RecordNotFoundException(String message, String field, String reason) {
        super(message, field, reason);
    }
}
