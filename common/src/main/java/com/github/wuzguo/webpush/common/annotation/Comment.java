package com.github.wuzguo.webpush.common.annotation;


import com.github.wuzguo.webpush.common.param.DefaultSample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注释注解
 *
 * @author wuzguo
 * @date 2016年11月14日 下午7:58:12
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    /**
     * 注解内容
     **/
    public String value() default "";

    /**
     * 名称
     **/
    public String name() default "";

    /**
     * 服务协议
     **/
    public String protocol() default "";

    /**
     * 服务风格
     **/
    public String style() default "";

    /**
     * url
     **/
    public String url() default "";

    /**
     * 必须的
     **/
    public boolean required() default false;

    /**
     * 业务规则
     **/
    public String business() default "";

    /**
     * 备注
     **/
    public String remark() default "";

    /**
     * 消费方
     **/
    public String consumer() default "";

    /**
     * 提供方
     **/
    public String provider() default "";

    /**
     * 示例
     **/
    public Class sample() default DefaultSample.class;

    /**
     * 请求示例
     **/
    public Class sampleReq() default DefaultSample.class;

    /**
     * 返回示例
     **/
    public Class sampleRes() default DefaultSample.class;

    /**
     * 请求示例
     **/
    public String sampleReqStr() default "";

    /**
     * 返回示例
     **/
    public String sampleResStr() default "";

    /**
     * 主键
     **/
    public boolean primaryKey() default false;

    /**
     * 请求方法
     **/
    public String requestMethod() default "";

}
