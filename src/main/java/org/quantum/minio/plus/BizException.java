package org.quantum.minio.plus;

import org.quantum.nucleus.component.dto.ResponseState;

public class BizException extends RuntimeException {

    public BizException(ResponseState responseState) {
        super(responseState.getMessage());
        this.code = responseState.getCode();
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
