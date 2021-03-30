package org.quantum.minio.plus.web.common;

import org.quantum.nucleus.component.dto.ResponseState;

public enum FailResponseStatus implements ResponseState {
    UNAUTHORIZED("WEB_UNAUTHORIZED", "未经授权"),
    ;

    private String code;

    private String message;

    FailResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
