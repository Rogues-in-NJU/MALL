package edu.nju.mall.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.dto.UnifiedOrderDTO;
import edu.nju.mall.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WechatPayService {

    @Value("${wechat.micro-program.app-id}")
    private String appId;
    @Value("${wechat.micro-program.app-secret}")
    private String appSecret;
    @Value("${wechat.pay.mch-id}")
    private String mchId;
    @Value("${wechat.pay.secret-key}")
    private String paySecretKey;

    @Value("${wechat.pay.notify-url}")
    private String notifyUrl;

    /**
     * 统一下单接口
     *
     * */
    public String unifiedOrder(@Nonnull UnifiedOrderDTO data) {
        Preconditions.checkNotNull(data);

        Map<String, Object> generateData = new HashMap<>();
        BeanUtil.copyProperties(data, generateData);

        generateData.put("appid", appId);
        generateData.put("mch_id", mchId);
        generateData.put("nonce_str", IdUtil.fastSimpleUUID());

        try {
            InetAddress ip = InetAddress.getLocalHost();
            generateData.put("spbill_create_ip", ip.getHostAddress());
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "微信统一下单失败!");
        }
        generateData.put("notify_url", notifyUrl);
        // 小程序取值为JSAPI
        generateData.put("trade_type", "JSAPI");
        generateData.put("sign", generateSign(generateData));

        log.debug("Generate Data: {}", JSON.toJSONString(generateData));
        String responseBody;
        try {
            responseBody = HttpRequest.post("https://api.mch.weixin.qq.com/pay/unifiedorder")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                    .body(XmlUtils.mapToXml(generateData))
                    .execute().body();
            return responseBody;
        } catch (TransformerException | IOException | ParserConfigurationException e) {
            log.error(e.getMessage(), e);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "微信统一下单失败!");
        }
    }

    /**
     * 查询订单接口
     * */
    public String orderQuery(@Nonnull Map<String, Object> data) {
        return null;
    }

    private String mapJoin(@Nonnull Map<String, Object> map) {
        Preconditions.checkNotNull(map);
        List<String> keyValues = map.keySet().stream()
                .sorted()
                .map(k -> k + "=" + JSON.toJSONString(map.get(k)))
                .collect(Collectors.toList());
        return StringUtils.join(keyValues, "&");
    }

    /**
     * 添加签名
     * */
    private String generateSign(@Nonnull Map<String, Object> originData) {
        Preconditions.checkNotNull(originData);
        String originStr = mapJoin(originData);
        String stringSignTemp = originStr + "&key=" + paySecretKey;
        String sign = SecureUtil.md5(stringSignTemp).toUpperCase();
        return sign;
    }

}
