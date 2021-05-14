package org.quantum.minio.plus;

/**
 * @author ike
 * @date 2021 年 05 月 13 日 19:40
 */
public class Response implements ResponseState {

    private String code;

    @Override
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public static Response of(ResponseState state){
        Response response = new Response();
        response.setCode(state.getCode());
        return response;
    }

    public static Response ok() {
        Response response = new Response();
        response.setCode("ok");
        return response;
    }
}
