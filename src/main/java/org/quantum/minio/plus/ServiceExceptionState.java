package org.quantum.minio.plus;

/**
 * @author ike
 * @date 2021 年 04 月 22 日 15:19
 */
public enum ServiceExceptionState implements ResponseState {
    BAD_PARAMETER("错误参数"),
    UNAUTHORIZED("未授权")
    ;

    ServiceExceptionState(String message) {
        this.message = message;
    }

    public String message;

    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return this.toString();
    }
}
