package org.quantum.minio.plus;

import org.quantum.nucleus.component.dto.ResponseState;

/**
 * @author ike
 * @date 2021 年 04 月 22 日 15:19
 */
public enum BizExceptionState implements ResponseState {
    PARAMETER_ERROR("PARAMETER_ERROR", "参数错误")
    ;

    private String code;

    private String message;

    BizExceptionState(String code, String message) {
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
