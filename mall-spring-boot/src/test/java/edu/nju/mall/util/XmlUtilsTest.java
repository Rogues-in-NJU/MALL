package edu.nju.mall.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import edu.nju.mall.dto.UnifiedOrderDTO;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlUtilsTest {

    @Test
    public void test() throws IOException, TransformerException, ParserConfigurationException, SAXException {
        Map<String, String> testData = new HashMap<>();
        testData.put("nonce_key", IdUtil.fastSimpleUUID());
        testData.put("mht_id", IdUtil.fastSimpleUUID());
        testData.put("secret_key", IdUtil.fastSimpleUUID());


        // System.out.println(JSON.toJSONString(XmlUtils.xmlToMap(XmlUtils.mapToXml(testData))));
    }

    @Test
    public void testBean() {
        UnifiedOrderDTO dto = UnifiedOrderDTO.builder()
                .body("test")
                .out_trade_no("test2")
                .total_fee(200).build();

        Map<String, Object> m = new HashMap<>();
        BeanUtil.copyProperties(dto, m);

        System.out.println(JSON.toJSONString(m));
    }

    @Test
    public void testJSON() {
        System.out.println(JSON.toJSONString(1));
    }

}
