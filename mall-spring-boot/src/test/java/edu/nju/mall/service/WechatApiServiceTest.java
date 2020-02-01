package edu.nju.mall.service;

import edu.nju.mall.common.WechatSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"dev", "devConn"})
public class WechatApiServiceTest {

    @Value("${wechat.micro-program.app-id}")
    private String appId;
    @Value("${wechat.micro-program.app-secret}")
    private String appSecret;

    private final String grantType = "authorization_code";

    @Autowired
    private WechatApiService wechatApiService;

    @Test
    public void testConnection() {
        WechatSession wechatSession = wechatApiService.jscode2Session(appId, appSecret, "test", grantType);
        System.out.println(wechatSession.getErrcode());
    }

}
