package com.testtask.nauka.common.exceptions;

public class RelationNotFoundException extends InvalidFieldsException {
    public RelationNotFoundException(String msg) {
        super(msg);
    }

    public RelationNotFoundException(String reason, String field) {
        super(reason, field, reason);
    }

    public RelationNotFoundException(String message, String field, String reason) {
        super(message, field, reason);
    }
}
