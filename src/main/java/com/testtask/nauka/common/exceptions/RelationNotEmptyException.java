package com.testtask.nauka.common.exceptions;

public class RelationNotEmptyException extends InvalidFieldsException {
    public RelationNotEmptyException(String msg) {
        super(msg);
    }

    public RelationNotEmptyException(String reason, String field) {
        super(reason, field, reason);
    }

    public RelationNotEmptyException(String message, String field, String reason) {
        super(message, field, reason);
    }
}