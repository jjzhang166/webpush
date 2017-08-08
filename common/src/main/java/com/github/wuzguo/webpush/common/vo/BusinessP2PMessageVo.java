package com.github.wuzguo.webpush.common.vo;


import com.github.wuzguo.webpush.common.annotation.Comment;

import java.io.Serializable;


/**
 * 业务消息点对点消息
 *
 * @author wuzguo
 * @date 2016年12月14日 下午5:10:31
 */
@Comment("业务点对点消息对象")
public class BusinessP2PMessageVo extends BaseMessageVo implements Serializable {
    /**
     * 业务实例id
     **/
    @Comment(value = "业务实例id")
    protected String id;
    /**
     * 业务标记
     **/
    @Comment(value = "业务标记")
    protected String biz;
    /**
     * 业务状态
     **/
    @Comment(value = "业务状态,1成功，0失败")
    protected String status;
    /**
     * 异常信息
     **/
    @Comment(value = "异常信息,status=1包含异常信息")
    protected String error;
    /**
     * 业务数据
     **/
    @Comment(value = "业务数据")
    protected Object data;

    public BusinessP2PMessageVo() {
        super();
    }

    public BusinessP2PMessageVo(String id, String biz, String status) {
        super();
        this.id = id;
        this.biz = biz;
        this.status = status;
    }

    public BusinessP2PMessageVo(String id, String biz, String status, String error) {
        super();
        this.id = id;
        this.biz = biz;
        this.status = status;
        this.error = error;
    }

    public BusinessP2PMessageVo(String id, String biz, String status, Object data) {
        super();
        this.id = id;
        this.biz = biz;
        this.status = status;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
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
        return "BusinessP2PMessageVo [id=" + id + ", biz=" + biz + ", status=" + status + ", error=" + error + ", data="
                + data + ", to=" + to + ", time=" + time + ", text=" + text + "]";
    }


}
