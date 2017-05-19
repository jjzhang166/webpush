package com.github.wuzguo.server.authenticate.jwt.sample;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.wuzguo.codegen.param.ISample;
import com.github.wuzguo.webpush.common.vo.JwtTokenUserVo;
import com.github.wuzguo.webpush.common.vo.JwtTokenVo;
import com.github.wuzguo.webpush.common.vo.ResultVo;


/**
 * 创建JWT令牌接口返回样例
 *
 * @author wuzguo
 * @date 2016年12月18日 下午6:19:51
 */
public class JwtTokenCreateSample implements ISample {

    @Override
    public String request() {
        JwtTokenUserVo result = new JwtTokenUserVo();
        String json = JSON.toJSONString(result, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return json;
    }

    @Override
    public String response() {
        ResultVo result = new ResultVo();
        result.setStatus("1");
        JwtTokenVo token = new JwtTokenVo();
        result.setData(token);

        String json = JSON.toJSONString(result, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return json;
    }

}
