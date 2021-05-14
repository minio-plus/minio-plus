package org.quantum.minio.plus;

/**
 * 对象响应
 * @author ike
 * @date 2021 年 05 月 13 日 19:15
 */
public class ValueResponse<T> extends Response {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ValueResponse<T> of(T data){
        ValueResponse<T> response = new ValueResponse<>();
        response.setCode("ok");
        response.setData(data);
        return response;
    }

    public static <T> ValueResponse<T> of(ResponseState state, T data){
        ValueResponse<T> response = new ValueResponse<>();
        response.setCode(state.getCode());
        response.setData(data);
        return response;
    }
}
