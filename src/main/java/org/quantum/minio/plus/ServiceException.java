package org.quantum.minio.plus;

/**
 * 服务异常
 * @author jpx10
 */
public class ServiceException extends RuntimeException implements ResponseState {

    public ServiceException(ServiceExceptionState state) {
        super(state.message);
        this.code = state.getCode();
    }

    private String code;

    @Override
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
