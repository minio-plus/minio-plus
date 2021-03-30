package org.quantum.minio.plus.dto;

/**
 * 用户登录 数据传输对象
 * @author ike
 * @date 2021 年 03 月 30 日 12:00
 */
public class UserLoginDTO {

    public String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
