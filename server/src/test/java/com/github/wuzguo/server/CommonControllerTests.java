package com.github.wuzguo.server;

import com.github.wuzguo.server.server.NettySocketIoServer;
import com.github.wuzguo.server.web.CommonController;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO CommonController 测试类
 *
 * @author wuzguo at 2017/5/9 15:44
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
@WebAppConfiguration()
@Profile("dev")
public class CommonControllerTests {

    private MockMvc mvc;

    @MockBean
    private NettySocketIoServer nettySocketIoServer;

    @InjectMocks
    private CommonController commonController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(commonController).build();
    }

    @Test
    public void testMessageTypes() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/common/types")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void tesActions() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/common/actions")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
