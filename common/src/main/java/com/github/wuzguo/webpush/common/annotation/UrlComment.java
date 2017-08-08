package com.github.wuzguo.webpush.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * url注解
 * 适合controller
 *
 * @author wuzguo
 * @date 2016年11月14日 下午7:58:12
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlComment {
    /**
     * url
     **/
    public String url() default "";

    /**
     * 注解内容
     **/
    public String value() default "";
}
