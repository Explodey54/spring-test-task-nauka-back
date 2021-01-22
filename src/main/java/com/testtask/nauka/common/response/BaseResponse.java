package com.testtask.nauka.common.response;

import lombok.Data;

@Data
abstract public class BaseResponse {
    public final boolean ok;

    public BaseResponse(boolean ok) {
        this.ok = ok;
    }
}
