package org.quantum.minio.plus;

import java.util.Collection;

/**
 * 分页响应
 * @author ike
 * @date 2021 年 05 月 13 日 19:35
 */
public class PageResponse<T> extends ValueResponse<Collection<T>> {

    private Long total;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public static <T> PageResponse<T> of(Collection<T> data, long total) {
        PageResponse<T> response = new PageResponse<>();
        response.setCode("ok");
        response.setTotal(total);
        response.setData(data);
        return response;
    }

    public static <T> PageResponse<T> of(Collection<T> data){
        return PageResponse.of(data, 0);
    }
}
