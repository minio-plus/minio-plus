package org.quantum.minio.plus.web.vo;

/**
 * 用户公开信息 视图对象
 * @author ike
 * @date 2021 年 03 月 30 日 15:17
 */
public class UserPublicInfoVO {

    private String avatar;

    private String username;

    private String[] permissions;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
