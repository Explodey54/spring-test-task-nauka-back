package com.testtask.nauka.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ErrorResponse extends BaseResponse {
    public final String reason;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, List<String>> fields = Collections.emptyMap();

    public ErrorResponse(String reason) {
        super(false);
        this.reason = reason;
    }

    public ErrorResponse(String reason, Map<String, List<String>> fields) {
        super(false);
        this.reason = reason;
        this.fields = fields;
    }
}
