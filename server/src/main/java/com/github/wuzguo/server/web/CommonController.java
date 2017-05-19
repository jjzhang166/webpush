package com.github.wuzguo.server.web;


import com.github.wuzguo.server.action.ActionDataListener;
import com.github.wuzguo.server.server.NettySocketIoServer;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.util.BeanUtil;
import com.github.wuzguo.webpush.common.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 接口信息接口
 *
 * @author wuzguo
 * @date 2016年12月18日 下午3:24:24
 */
@RestController
@RequestMapping(value = "/api/common")
public class CommonController {

    @Autowired
    private NettySocketIoServer server;


    /**
     * 消息类型列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/types")
    public Object messageTypes(final HttpServletRequest request, final HttpServletResponse response) {
        Map types = new LinkedHashMap();
        for (MessageTypeEnum row : MessageTypeEnum.values()) {
            types.put(row.name(), row.getMessageName());
        }
        return new ResultVo("1", types);
    }


    /**
     * 动作集合
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/actions")
    public Object actions(final HttpServletRequest request, final HttpServletResponse response) {
        Map actions = new LinkedHashMap();
        for (ActionDataListener action : server.getActionDataListeners()) {
            actions.put(action.getActionName(), BeanUtil.findMethod(action.getDataClass(), "set", true));
        }
        return new ResultVo("1", actions);
    }
}
