package com.testtask.nauka.common.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvalidFieldsException extends RuntimeException {
    @Getter
    private final Map<String, List<String>> fields = new HashMap<>();

    public InvalidFieldsException(String message) {
        super(message);
    }

    public InvalidFieldsException(String message, String field, String reason) {
        super(message);
        addError(field, reason);
    }

    public void addError(String fieldKey, String error) {
        boolean hasField = fields.containsKey(fieldKey);
        if (!hasField) {
            fields.put(fieldKey, new ArrayList<>());
        }
        fields.get(fieldKey).add(error);
    }
}
