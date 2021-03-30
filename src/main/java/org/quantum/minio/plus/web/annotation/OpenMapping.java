package org.quantum.minio.plus.web.annotation;

import java.lang.annotation.*;

/**
 * @author ike
 * @date 2021 年 03 月 30 日 15:23
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenMapping {

    boolean required() default true;

}
