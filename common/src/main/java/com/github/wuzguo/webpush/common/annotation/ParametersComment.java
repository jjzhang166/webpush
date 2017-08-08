package com.github.wuzguo.webpush.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数注解
 *
 * @author wuzguo
 * @date 2016年11月14日 下午7:58:12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersComment {
    /**
     * 参数名称
     **/
    public String[] name() default "";

    /**
     * 是否必填参数
     **/
    public boolean[] required() default false;

    /**
     * 注解内容
     **/
    public String[] value() default "";

    /**
     * 参数详情
     **/
    public Class[] detail() default String.class;
}
