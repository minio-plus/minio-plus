package org.quantum.minio.plus.dto;

/**
 * 认证 数据传输对象
 * @author ike
 * @date 2021 年 03 月 30 日 14:43
 */
public class AuthDTO {

    private String accessToken;

    private Long expiresAt;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
