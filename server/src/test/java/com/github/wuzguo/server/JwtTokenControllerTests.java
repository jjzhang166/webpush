package com.github.wuzguo.server;

import com.alibaba.fastjson.JSON;
import com.github.wuzguo.server.authenticate.jwt.config.JwtConfig;
import com.github.wuzguo.server.authenticate.jwt.util.JwtUtil;
import com.github.wuzguo.server.authenticate.jwt.web.JwtTokenController;
import com.github.wuzguo.server.server.NettySocketIoServer;
import com.github.wuzguo.webpush.common.vo.JwtTokenUserVo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO JwtTokenController 测试类
 *
 * @author wuzguo at 2017/5/9 9:58
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
@WebAppConfiguration()
@Profile("dev")
public class JwtTokenControllerTests {

    private MockMvc mvc;

    @MockBean
    private NettySocketIoServer nettySocketIoServer;

    @Spy
    private JwtConfig jwtConfig = new JwtConfig();

    @Spy
    private JwtUtil jwtUtil = new JwtUtil();

    @InjectMocks
    public JwtTokenController jwtTokenController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        jwtConfig.setSignKey("%$t7Lg8e3#wDr8$23&h423");
        jwtConfig.setClaimKey("webpush");

        jwtUtil.setJwtConfig(jwtConfig);

        mvc = MockMvcBuilders.standaloneSetup(jwtTokenController).build();
    }

    @Test
    public void getToken() throws Exception {
        JwtTokenUserVo tokenUserVo = new JwtTokenUserVo("1234", "tomcat");
        String tokenJson = JSON.toJSONString(tokenUserVo);
        System.out.println("tokenJson: " + tokenJson);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/token")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // 使用content方法，将转换的json数据放到request的body中
                .content(tokenJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    public void refreshToken() throws Exception {
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0OTQzMDE5ODMsImlzcyI6IndlYnB1c2giLCJuYmYiOjE0OTQzMDA" +
                "xODMsInVzZXIiOiJ7XCJuYW1lXCI6XCJ0b21jYXRcIixcInVpZFwiOlwiMTIzNFwifSJ9." +
                "i5_ffJvlOotD6Qsx_QuUqsTYoX-p8-oUN5ggKyA9KBs";

        Map mapToken = new HashMap<String, String>(2) {{
            put("token", token);
        }};

        String tokenJson = JSON.toJSONString(mapToken);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/refresh")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // 直接传String,而不是JSON
                .content(token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
