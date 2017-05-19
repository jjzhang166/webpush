package com.github.wuzguo.server;

import com.alibaba.fastjson.JSON;
import com.github.wuzguo.server.server.NettySocketIoServer;
import com.github.wuzguo.server.web.MessageController;
import com.github.wuzguo.webpush.common.vo.BusinessP2PMessageVo;
import com.github.wuzguo.webpush.common.vo.GroupMessageVo;
import com.github.wuzguo.webpush.common.vo.P2PMessageVo;
import com.github.wuzguo.webpush.common.vo.SystemP2PMessageVo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO MessageController控制器测试
 *
 * @author wuzguo at 2017/5/9 16:05
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
@WebAppConfiguration()
@Profile("dev")
public class MessageControllerTests {

    private MockMvc mvc;

    @MockBean
    private NettySocketIoServer nettySocketIoServer;

    @InjectMocks
    private MessageController messageController;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    public void testP2Pmessage() throws Exception {
        P2PMessageVo messageVo = new P2PMessageVo("tom", System.currentTimeMillis(), "hello，jerry", "jerry");
        String messageJson = JSON.toJSONString(messageVo);
        System.out.println("messageJson: " + messageJson);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/message/p2p/123456")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // 使用content方法，将转换的json数据放到request的body中
                .content(messageJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    public void testSystemP2Pmessage() throws Exception {
        SystemP2PMessageVo messageVo = new SystemP2PMessageVo("tom", System.currentTimeMillis(), "hello，everyboby", "console");
        String messageJson = JSON.toJSONString(messageVo);
        System.out.println("messageJson: " + messageJson);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/message/sysp2p/123456")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // 使用content方法，将转换的json数据放到request的body中
                .content(messageJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    public void testGroupMessage() throws Exception {
        GroupMessageVo messageVo = new GroupMessageVo("tom", System.currentTimeMillis(), "hello，everyboby", "friends", "jerry");
        String messageJson = JSON.toJSONString(messageVo);
        System.out.println("messageJson: " + messageJson);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/message/group/123456")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // 使用content方法，将转换的json数据放到request的body中
                .content(messageJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    public void testBizp2pMessage() throws Exception {
        BusinessP2PMessageVo messageVo = new BusinessP2PMessageVo("SID1001", "user", "0");
        String messageJson = JSON.toJSONString(messageVo);
        System.out.println("messageJson: " + messageJson);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/message/bizp2p/123456")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // 使用content方法，将转换的json数据放到request的body中
                .content(messageJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
