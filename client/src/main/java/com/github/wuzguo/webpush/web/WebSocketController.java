
package com.github.wuzguo.webpush.web;

import com.github.wuzguo.codegen.annotation.Comment;
import com.github.wuzguo.codegen.annotation.ParametersComment;
import com.github.wuzguo.codegen.annotation.ReturnComment;
import com.github.wuzguo.webpush.common.base.Result;
import com.github.wuzguo.webpush.common.base.ReturnMsg;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.exception.FrameException;
import com.github.wuzguo.webpush.common.vo.P2PMessageVo;
import com.github.wuzguo.webpush.common.vo.ResultVo;
import com.github.wuzguo.webpush.service.IWebSocketService;
import com.github.wuzguo.webpush.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 测试用的，调服务端的接口，实际项目中可以通过NGINX做URL重写解决前台的跨域问题。
 *
 * @author wuzguo at 2017/5/11 10:48
 */

@RestController
@RequestMapping(value = "/socket/api")
public class WebSocketController {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private IWebSocketService webSocketService;

    //这里返回 "indedx" 字符串
    @RequestMapping(value = "/")
    public String index(final HttpServletRequest request, final HttpServletResponse response) {
        String viewName = "index";
        return viewName;
    }

    //这里返回 index.html
    @RequestMapping(value = "/index",
            method = RequestMethod.GET)
    public ModelAndView indexView(final HttpServletRequest request, final HttpServletResponse response) {
        String viewName = "index";
        ModelAndView view = new ModelAndView(viewName);
        return view;
    }

    @RequestMapping(value = {"/token"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result token(@RequestBody final User user, final HttpServletRequest request) {
        Result result = new Result("1", System.currentTimeMillis() + "");
        result.setRequestID(request.getRequestedSessionId());
        result.setReturnMsg(new ReturnMsg("1", "获取Token信息失败"));

        if (user == null || StringUtils.isEmpty(user.getUid()) || StringUtils.isEmpty(user.getName())) {
            return result;
        }

        ResultVo resultVo = null;

        try {
            resultVo = webSocketService.tokenGen(user);
        } catch (FrameException e) {
            LOGGER.error("token: " + e.getErrCode() + ", " + e.getErrMsg());
        }

        if (resultVo != null) {
            result.setReturnTag("0");
            result.setReturnMsg(new ReturnMsg("0", "成功"));
            result.setData(resultVo.getData());
        }

        return result;
    }


    @RequestMapping(value = {"/refresh"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result refresh(@RequestBody final String token, final HttpServletRequest request) {
        Result result = new Result("1", System.currentTimeMillis() + "");
        result.setRequestID(request.getRequestedSessionId());
        result.setReturnMsg(new ReturnMsg("1", "Token刷新信息失败"));

        // 如果为空
        if (StringUtils.isEmpty(token)) {
            return result;
        }

        ResultVo resultVo = null;

        try {
            resultVo = webSocketService.refreshToken(token);
        } catch (FrameException e) {
            LOGGER.error("token: " + e.getErrCode() + ", " + e.getErrMsg());
        }

        if (resultVo != null) {
            result.setReturnTag("0");
            result.setReturnMsg(new ReturnMsg("0", "成功"));
        }

        return result;
    }


    /**
     * 点对点消息
     *
     * @param userId
     * @param message
     * @return
     */
    @RequestMapping(value = "/p2p/{userId}", method = RequestMethod.POST)
    public Object p2p(@PathVariable final String userId, @RequestBody final P2PMessageVo message, final HttpServletRequest request) {
        Result result = new Result("1", System.currentTimeMillis() + "");
        result.setRequestID(request.getRequestedSessionId());
        result.setReturnMsg(new ReturnMsg("1", "发送消息失败"));

        if (StringUtils.isEmpty(userId) || message == null) {
            return result;
        }

        ResultVo resultVo = null;
        try {
            resultVo = webSocketService.sendP2PMessage(message, userId);
        } catch (FrameException e) {
            LOGGER.error("token: " + e.getErrCode() + ", " + e.getErrMsg());
        }

        if (resultVo != null) {
            result.setReturnTag("0");
            result.setReturnMsg(new ReturnMsg("0", "成功"));
            result.setData(resultVo.getData());
        }

        return result;
    }

}
