package com.github.wuzguo.server.authenticate.jwt.web;


import com.github.wuzguo.server.authenticate.jwt.sample.JwtTokenCreateSample;
import com.github.wuzguo.server.authenticate.jwt.util.JwtUtil;
import com.github.wuzguo.webpush.common.annotation.Comment;
import com.github.wuzguo.webpush.common.annotation.ParametersComment;
import com.github.wuzguo.webpush.common.annotation.ReturnComment;
import com.github.wuzguo.webpush.common.vo.JwtTokenUserVo;
import com.github.wuzguo.webpush.common.vo.JwtTokenVo;
import com.github.wuzguo.webpush.common.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * jwt令牌 Controller
 *
 * @author wuzguo
 * @date 2016年12月16日 下午6:06:14
 */
@Comment(value = "JWT令牌RESTful接口", protocol = "http",
        url = "http://ip:port/api/token",
        style = "RESTful",
        provider = "MPS",
        consumer = "ALL")
@RestController
@RequestMapping(value = "/api")
public class JwtTokenController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenController.class);

    @Autowired
    private JwtUtil jwtUtil;

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }

    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    /**
     * 生成token
     * <p>
     * RequestBody 注解要求客户端需要以JSON格式传递参数
     * 直接通过浏览器输入url时，@RequestBody获取不到json对象，
     * 需要用java编程或者基于ajax的方法请求，将Content-Type设置为application/json
     * </p>
     *
     * @param userVo  JWT用户对象
     * @param request
     * @return
     */
    @Comment(value = "创建JWT令牌接口",
            url = "/token",
            requestMethod = "POST",
            sample = JwtTokenCreateSample.class,
            business = "令牌默认30分钟失效,令牌刷新期间为失效前10分钟内.")
    @ParametersComment(name = "JSON String",
            value = "授权用户参数",
            required = true,
            detail = JwtTokenUserVo.class)
    @ReturnComment(type = JwtTokenVo.class, value = "data", isArray = false)
    @RequestMapping(value = {"/token"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object token(@RequestBody final JwtTokenUserVo userVo, final HttpServletRequest request) {
        String token = null;
        try {
            token = jwtUtil.create(userVo);
        } catch (Exception e) {
            LOGGER.error("生成jwt签名失败", e);
            return new ResultVo("0", e.getMessage());
        }
        JwtTokenVo tokenVo = new JwtTokenVo(token, JwtUtil.lastExpires(token) / 1000);
        return new ResultVo("1", tokenVo);
    }

    /**
     * 刷新令牌(超时时间长返回原令牌，将近超时时间产生新的令牌)
     *
     * @param token
     * @return
     */
    @Comment(value = "刷新JWT令牌接口",
            url = "/refresh",
            requestMethod = "PUT",
            sampleReq = JwtTokenVo.class,
            sampleRes = ResultVo.class,
            business = "旧令牌在未失效之前并在令牌刷新期间,根据原来的用户信息产生新的令牌。若不在刷新期间返回原令牌.")
    @ParametersComment(name = "JSON String",
            value = "令牌参数",
            required = true,
            detail = JwtTokenVo.class)
    @ReturnComment(type = JwtTokenVo.class, value = "data", isArray = false)
    @RequestMapping(value = {"/refresh"},
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object refresh(@RequestBody final String token, final HttpServletRequest request) {
        String newToken = null;
        try {
            newToken = jwtUtil.refresh(token);
        } catch (Exception e) {
            LOGGER.error("刷新jwt签名失败", e);
            return new ResultVo("0", e.getMessage());
        }

        JwtTokenVo tokenVo = new JwtTokenVo(newToken, !StringUtils.equals(token, newToken), JwtUtil.lastExpires(newToken) / 1000);
        return new ResultVo("1", tokenVo);
    }


    /**
     * 测试用例
     *
     * @param name
     * @param request
     * @return
     */
    @RequestMapping(value = {"/hello/{name}"},
            method = RequestMethod.GET)
    public Object hello(@PathVariable(name = "name") final String name, final HttpServletRequest request) {
        return "Hello, " + name;
    }
}
