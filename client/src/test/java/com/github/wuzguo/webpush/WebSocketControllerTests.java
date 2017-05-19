
package com.github.wuzguo.webpush;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * TODO WebSocketController 测试类
 *
 * @author wuzguo at 2017/5/11 14:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
@WebAppConfiguration()
public class WebSocketControllerTests {

//    @Autowired
//    private IWebSocketService webSocketService;
//
//    @Test
//    public void sendMessage() throws FrameException {
//        P2PMessageVo messageVo = new P2PMessageVo("Tom", "Jerry");
//        messageVo.setText("Hello, Tom");
//        messageVo.setTime(System.currentTimeMillis());
//        webSocketService.sendP2PMessage(messageVo);
//    }
}
