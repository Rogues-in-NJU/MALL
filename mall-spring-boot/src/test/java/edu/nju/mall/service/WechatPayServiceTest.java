package edu.nju.mall.service;

import com.alibaba.fastjson.JSON;
import edu.nju.mall.dto.UnifiedOrderDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"dev", "devConn"})
public class WechatPayServiceTest {

    @Autowired
    private WechatPayService wechatPayService;

    @Test
    public void test() {
        UnifiedOrderDTO unifiedOrderDTO = UnifiedOrderDTO.builder()
                .body("")
                .out_trade_no("1234")
                .total_fee(1000)
                .build();
        System.out.println(JSON.toJSONString(wechatPayService.unifiedOrder(unifiedOrderDTO)));
    }

    @Test
    public void testSandbox() {
        System.out.println(wechatPayService.getSandboxSign());
    }

}
