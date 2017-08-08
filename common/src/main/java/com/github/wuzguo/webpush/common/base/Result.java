package com.github.wuzguo.webpush.common.base;


import com.github.wuzguo.webpush.common.annotation.Comment;

import java.io.Serializable;


/**
 * 返回结果
 */

@Comment(value = "返回结果对象")
public class Result implements Serializable {

    private static final long serialVersionUID = 1558506851955724524L;

    /**
     * 返回状态标识。0-成功，1-失败
     */
    @Comment(value = "返回状态标识。0-成功，1-失败")
    private String returnTag;

    @Comment(value = "时间戳")
    private String timestamp;

    @Comment(value = "业务请求ID")
    private String requestID;

    @Comment(value = "返回消息")
    private ReturnMsg returnMsg;

    @Comment(value = "信息封装类")
    private Object data;

    public Result() {
    }

    public Result(String returnTag, String timestamp) {
        this.returnTag = returnTag;
        this.timestamp = timestamp;
    }

    public Result(String returnTag, String timestamp, ReturnMsg returnMsg) {
        this.returnTag = returnTag;
        this.timestamp = timestamp;
        this.returnMsg = returnMsg;
    }

    public String getReturnTag() {
        return returnTag;
    }

    public void setReturnTag(String returnTag) {
        this.returnTag = returnTag;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public ReturnMsg getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(ReturnMsg returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "returnTag='" + returnTag + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", requestID='" + requestID + '\'' +
                ", returnMsg=" + returnMsg +
                ", data=" + data +
                '}';
    }
}
