package org.quantum.minio.plus.web.context;

import org.springframework.core.NamedThreadLocal;

/**
 * @author ike
 * @date 2021 年 03 月 30 日 15:28
 */
public class UserContextHolder {

    private static final ThreadLocal<String> userContextHolder = new NamedThreadLocal("userId");

    public static String get() {
        return userContextHolder.get();
    }

    public static void set(String userId) {
        userContextHolder.set(userId);
    }

    public static void remove() {
        userContextHolder.remove();
    }

}
