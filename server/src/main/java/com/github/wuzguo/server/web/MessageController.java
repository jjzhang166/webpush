package com.github.wuzguo.server.web;


import com.github.wuzguo.webpush.common.annotation.Comment;
import com.github.wuzguo.webpush.common.annotation.ParametersComment;
import com.github.wuzguo.webpush.common.annotation.ReturnComment;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.server.ISocketIoServer;
import com.github.wuzguo.webpush.common.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息推送api接口
 *
 * @author wuzguo
 * @date 2016年12月18日 下午3:24:10
 */
@Comment(value = "消息推送RESTful接口",
        protocol = "http",
        url = "http://ip:port/api/message",
        style = "RESTful",
        provider = "MPS",
        consumer = "All")
@RestController
@RequestMapping(value = "/api/message")
public class MessageController {

    @Autowired
    private ISocketIoServer server;

    /**
     * 点对点消息
     *
     * @param userId
     * @param message
     * @return
     */
    @RequestMapping(value = "/p2p/{userId}", method = RequestMethod.POST)
    @Comment(value = "点对点消息推送接口",
            url = "/p2p/{userId}",
            sampleReq = P2PMessageVo.class,
            sampleRes = ResultVo.class,
            business = "用户发送消息给另外一个用户使用.")
    @ParametersComment(name = "JSON String", value = "消息参数", required = true, detail = P2PMessageVo.class)
    @ReturnComment(type = ResultVo.class, value = "data", isArray = false)
    public Object p2p(@PathVariable final String userId, @RequestBody final P2PMessageVo message) {
        if (StringUtils.isEmpty(userId) || message == null) {
            return new ResultVo("0", "发送失败");
        }
        setDefaultParams(message);
        return sendMessage(MessageTypeEnum.p2p, userId, message);
    }

    /**
     * 系统点对点消息
     *
     * @param userId
     * @param message
     * @return
     */
    @RequestMapping(value = "/sysp2p/{userId}", method = RequestMethod.POST)
    @Comment(value = "系统点对点消息推送接口",
            url = "/sysp2p/{userId}",
            sampleReq = SystemP2PMessageVo.class,
            sampleRes = ResultVo.class,
            business = "系统推送系统消息指定用户使用.")
    @ParametersComment(name = "JSON String", value = "消息参数", required = true, detail = SystemP2PMessageVo.class)
    @ReturnComment(type = ResultVo.class, value = "data", isArray = false)
    public Object systemP2p(@PathVariable final String userId, @RequestBody final SystemP2PMessageVo message) {
        if (StringUtils.isEmpty(userId) || message == null) {
            return new ResultVo("0", "发送失败");
        }

        setDefaultParams(message);
        return sendMessage(MessageTypeEnum.sysp2p, userId, message);
    }

    /**
     * 业务点对点消息
     *
     * @param userId
     * @param message
     * @return
     */
    @RequestMapping(value = "/bizp2p/{userId}", method = RequestMethod.POST)
    @Comment(value = "业务点对点消息推送接口",
            url = "/bizp2p/{userId}",
            sampleReq = BusinessP2PMessageVo.class,
            sampleRes = ResultVo.class,
            business = "系统推送业务消息指定用户使用.")
    @ParametersComment(name = "JSON String", value = "消息参数", required = true, detail = BusinessP2PMessageVo.class)
    @ReturnComment(type = ResultVo.class, value = "data", isArray = false)
    public Object systemP2p(@PathVariable final String userId, @RequestBody final BusinessP2PMessageVo message) {
        if (StringUtils.isEmpty(userId) || message == null) {
            return new ResultVo("0", "发送失败");
        }

        setDefaultParams(message);
        return sendMessage(MessageTypeEnum.bizp2p, userId, message);
    }

    /**
     * 发送群组消息
     *
     * @param groupId
     * @param message
     * @return
     */
    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.POST)
    @Comment(value = "群组消息推送接口",
            url = "/group/{groupId}",
            sampleReq = GroupMessageVo.class,
            sampleRes = ResultVo.class,
            business = "推送群组消息指定群组使用.")
    @ParametersComment(name = "JSON String", value = "消息参数", required = true, detail = GroupMessageVo.class)
    @ReturnComment(type = ResultVo.class, value = "data", isArray = false)
    public Object group(@PathVariable final String groupId, @RequestBody final GroupMessageVo message) {
        if (StringUtils.isEmpty(groupId) || message == null) {
            return new ResultVo("0", "发送失败");
        }

        setDefaultParams(message);
        String status = server.sendGroupMessage(groupId, message, true);
        if ("0".equals(status)) {
            return new ResultVo("0", "群组不存在");
        }
        return new ResultVo("1");
    }

    /**
     * 系统消息(广播)
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/system", method = RequestMethod.POST)
    @Comment(value = "系统广播消息推送接口",
            url = "/system",
            sampleReq = SystemMessageVo.class,
            sampleRes = ResultVo.class,
            business = "系统广播消息到所有用户使用.")
    @ParametersComment(name = "JSON String", value = "消息参数", required = true, detail = SystemMessageVo.class)
    @ReturnComment(type = ResultVo.class, value = "data", isArray = false)
    public Object system(@RequestBody final SystemMessageVo message) {
        if (message == null) {
            return new ResultVo("0", "发送失败");
        }

        setDefaultParams(message);
        server.sendSystemMessage(message, true);
        return new ResultVo("1");
    }

    /**
     * 发送消息
     *
     * @param msgType
     * @param uid
     * @param msgVo
     * @return
     */
    public ResultVo sendMessage(final MessageTypeEnum msgType, final String uid, final BaseMessageVo msgVo) {
        if (StringUtils.isEmpty(uid) || msgVo == null) {
            return new ResultVo("0", "发送失败");
        }

        String result = server.sendP2pMessage(msgType, uid, msgVo, true, true);
        if ("1".equals(result)) {
            return new ResultVo("1");
        } else if ("2".equals(result)) {
            return new ResultVo("1", "cached message");
        } else {
            return new ResultVo("0", "用户不在线");
        }
    }

    /**
     * 设置默认参数
     *
     * @param msgVo 消息内容
     */
    private static void setDefaultParams(final BaseMessageVo msgVo) {
        if (msgVo == null) {
            return;
        }
        if (msgVo.getTime() == 0) {
            msgVo.setTime(System.currentTimeMillis());
        }
    }

}
