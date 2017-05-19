package com.github.wuzguo.webpush.common.base;


import com.github.wuzguo.codegen.annotation.Comment;

import java.io.Serializable;

/**
 * 返回消息结果
 *
 * @author : wuzguo
 * @date: 2016年8月4日
 */
@Comment(value = "返回消息结果对象")
public class ReturnMsg implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -607062185957707543L;

    /**
     * 错误编码，如returnTag返回标识为0，则可为空，如returnTag返回标识为1，则必填。
     */
    @Comment(value = "错误编码，如returnTag返回标识为0，则可为空，如returnTag返回标识为1，则必填")
    private String errCode;

    /**
     * 错误描述
     */
    @Comment(value = "错误描述")
    private String errMsg;

    /**
     * 默认构造函数 errcode+errmsg为空 有错误时重新赋值
     */
    public ReturnMsg() {
        this.errCode = "";
        this.errMsg = "";
    }

    public ReturnMsg(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "ReturnMsg{" +
                "errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
