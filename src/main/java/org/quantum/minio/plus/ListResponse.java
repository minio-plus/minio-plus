package org.quantum.minio.plus;

import java.util.List;

/**
 * 列表响应
 * @author ike
 * @date 2021 年 05 月 14 日 9:05
 */
public class ListResponse<T> extends Response {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static <T> ListResponse<T> of(List<T> data){
        ListResponse<T> response = new ListResponse<>();
        response.setCode("ok");
        response.setData(data);
        return ListResponse.of(data);
    }
}
