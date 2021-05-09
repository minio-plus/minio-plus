package org.quantum.minio.plus.dto;

/**
 * @author ike
 * @date 2021 年 05 月 09 日 17:14
 */
public class PresignDTO {

    private String location;

    private String url;

    private Long expiration;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
