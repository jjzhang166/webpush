package com.github.wuzguo.webpush.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 返回注解
 * 用户被包装的真实返回参数类型
 *
 * @author wuzguo
 * @date 2016年11月14日 下午7:58:12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnComment {
    /**
     * 返回参数真实类型
     **/
    public Class type();

    /**
     * 注解内容
     **/
    public String value() default "";

    /**
     * 是否是集合
     **/
    public boolean isArray() default false;
}
