package edu.nju.mall.service;

import com.alibaba.fastjson.JSON;
import edu.nju.mall.util.XmlUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"dev", "devConn"})
public class WechatPayServiceTest {

    @Autowired
    private WechatPayService wechatPayService;

    @Test
    public void test() throws IOException, SAXException, ParserConfigurationException {
        Map<String, Object> testData = new HashMap<>();
        testData.put("appid", "123456");
        testData.put("appsecret", "34234");
        System.out.println(JSON.toJSONString(XmlUtils.xmlToMap(wechatPayService.unifiedOrder(null))));
    }

}
