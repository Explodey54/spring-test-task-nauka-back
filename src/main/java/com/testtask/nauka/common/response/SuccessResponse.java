package com.testtask.nauka.common.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class SuccessResponse<T> extends BaseResponse {
    public T data;

    public SuccessResponse(T data) {
        super(true);
        this.data = data;
    }
}
