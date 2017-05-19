
package com.github.wuzguo.webpush.service;

import com.github.wuzguo.webpush.common.exception.FrameException;
import com.github.wuzguo.webpush.common.vo.JwtTokenVo;
import com.github.wuzguo.webpush.common.vo.P2PMessageVo;
import com.github.wuzguo.webpush.common.vo.ResultVo;
import com.github.wuzguo.webpush.vo.User;

/**
 * TODO  调服务端的接口的服务接口
 *
 * @author wuzguo at 2017/5/11 10:56
 */

public interface IWebSocketService {

    /**
     * 获取Token
     *
     * @param user 用户对象
     * @return
     * @throws FrameException 异常
     */
    ResultVo tokenGen(User user) throws FrameException;

    /**
     * 更新Token
     *
     * @param token token对象
     * @return
     * @throws FrameException 异常
     */
    ResultVo refreshToken(String token) throws FrameException;


    /**
     * 发送P2P消息
     *
     * @param messageVo 消息体
     * @param userId    用户ID
     * @return
     * @throws FrameException 异常
     */
    ResultVo sendP2PMessage(P2PMessageVo messageVo, String userId) throws FrameException;
}
