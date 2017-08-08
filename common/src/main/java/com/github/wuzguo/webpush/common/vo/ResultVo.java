package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.webpush.common.annotation.Comment;

import java.io.Serializable;

/**
 * 返回结果vo
 *
 * @author wuzguo
 * @date 2016年12月16日 下午5:12:16
 */
@Comment("请求返回结果对象")
public class ResultVo implements Serializable {
    /**
     * 状态：1成功，0失败
     **/
    @Comment(value = "请求结果,1成功，0失败", required = true)
    private String status;
    /**
     * 异常信息
     **/
    @Comment(value = "异常信息,status=1时附带异常信息", required = false)
    private String error;
    /**
     * 数据
     **/
    @Comment(value = "业务数据对象", required = false)
    private Object data;

    public ResultVo() {
        super();
    }

    public ResultVo(String status) {
        super();
        this.status = status;
    }

    public ResultVo(String status, Object data) {
        super();
        this.status = status;
        this.data = data;
    }

    public ResultVo(String status, String error) {
        super();
        this.status = status;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVo [status=" + status + ", error=" + error + ", data=" + data + "]";
    }


}
