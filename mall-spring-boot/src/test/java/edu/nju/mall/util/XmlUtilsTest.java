package edu.nju.mall.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import edu.nju.mall.dto.UnifiedOrderDTO;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

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

        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        System.out.println(snowflake.nextId());
    }

    @Test
    public void testRepeat() {
        Set<Long> set = new HashSet<>();
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        CountDownLatch latch = new CountDownLatch(10000);
        Runnable work = () -> {
            // try {
            //     latch.await();
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
            long id = snowflake.nextId();
            if (set.contains(id)) {
                System.out.println("重复id: " + id);
            } else {
                // System.out.println(id);
                set.add(id);
            }
        };
        for (int i = 0; i < 10000; i++) {
            new Thread(work).start();
        }
    }

}
