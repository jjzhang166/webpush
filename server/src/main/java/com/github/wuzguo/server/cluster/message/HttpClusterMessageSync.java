package com.github.wuzguo.server.cluster.message;


import com.github.wuzguo.codegen.annotation.Comment;
import com.github.wuzguo.codegen.annotation.ParametersComment;
import com.github.wuzguo.codegen.annotation.ReturnComment;
import com.github.wuzguo.webpush.common.cluster.IClusterMessageSync;
import com.github.wuzguo.webpush.common.enums.MessageTypeEnum;
import com.github.wuzguo.webpush.common.server.ISocketIoServer;
import com.github.wuzguo.webpush.common.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * jms集群消息同步
 *
 * @author wuzguo
 * @date 2016年12月19日 下午7:36:28
 */
//@Component
@RestController
@RequestMapping(value = "/sync/message")
public class HttpClusterMessageSync implements IClusterMessageSync {

    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(HttpClusterMessageSync.class);

    @Autowired
    private ISocketIoServer server;


    @Override
    public void p2pMessage(final MessageTypeEnum msgType, final String uid, final BaseMessageVo msgVo) {
        server.sendP2pMessage(msgType, uid, msgVo, false, false);
    }

    @Override
    public void groupMessage(final String groupId, final GroupMessageVo msgVo) {
        server.sendGroupMessage(groupId, msgVo, false);

    }

    @Override
    public void systemMessage(final SystemMessageVo msgVo) {
        server.sendSystemMessage(msgVo, false);
    }

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
        p2pMessage(MessageTypeEnum.p2p, userId, message);
        return new ResultVo("1");
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
        p2pMessage(MessageTypeEnum.sysp2p, userId, message);
        return new ResultVo("1");
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
        p2pMessage(MessageTypeEnum.bizp2p, userId, message);
        return new ResultVo("1");
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
        groupMessage(groupId, message);
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
        systemMessage(message);
        return new ResultVo("1");
    }

}
