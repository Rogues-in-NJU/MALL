package edu.nju.mall.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
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

    @Test
    public void testSign() {
        System.out.println(SecureUtil.md5("appid=wx914c89b261bb6d56&mch_id=1566188611&nonce_str=740b1f102d8846949eadb472537c187d&notify_url=https://xxx.com&out_trade_no=1234&spbill_create_ip=192.168.0.101&total_fee=1000&trade_type=JSAPI&key=07a7e33f6b230559e3dc11a0f70c0d34").toUpperCase());
    }

}
