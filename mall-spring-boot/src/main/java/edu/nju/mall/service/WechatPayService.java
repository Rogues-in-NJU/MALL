package edu.nju.mall.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.dto.OrderQueryResponseDTO;
import edu.nju.mall.dto.SandboxSignResponseDTO;
import edu.nju.mall.dto.UnifiedOrderDTO;
import edu.nju.mall.dto.UnifiedOrderResponseDTO;
import edu.nju.mall.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WechatPayService {

    @Value("${wechat.micro-program.app-id}")
    private String appId;
    @Value("${wechat.micro-program.app-secret}")
    private String appSecret;
    @Value("${wechat.pay.mch-id}")
    private Long mchId;
    @Value("${wechat.pay.secret-key}")
    private String paySecretKey;

    @Value("${wechat.pay.notify-url}")
    private String notifyUrl;

    @Value("${wechat.pay.url.unified-order}")
    private String unifiedOrderUrl;
    @Value("${wechat.pay.url.order-query}")
    private String orderQueryUrl;
    @Value("${wechat.pay.url.close-order}")
    private String closeOrderUrl;
    @Value("${wechat.pay.url.sandbox-sign}")
    private String sandboxSignUrl;

    /**
     * 统一下单接口
     */
    public UnifiedOrderResponseDTO unifiedOrder(@Nonnull final UnifiedOrderDTO data) {
        Preconditions.checkNotNull(data);

        Map<String, Object> generateData = new HashMap<>();
        BeanUtil.copyProperties(data, generateData);
        Set<String> keys = new HashSet<>(generateData.keySet());
        for (String key : keys) {
            if (Objects.isNull(generateData.get(key)) || StringUtils.isBlank(String.valueOf(generateData.get(key)))) {
                generateData.remove(key);
            }
        }

        generateData.put("appid", appId);
        generateData.put("mch_id", mchId);

        //加密方式
        generateData.put("sign_type", "MD5");

        String nonceStr = IdUtil.fastSimpleUUID();
        generateData.put("nonce_str", nonceStr);
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
        if (StringUtils.isBlank(sandboxSignUrl)) {
            generateData.put("sign", generateSign(generateData, paySecretKey));
        } else {
            String sandboxSignKey = getSandboxSign();
            generateData.put("sign", generateSign(generateData, sandboxSignKey));
        }

        log.debug("Generate Data: {}", JSON.toJSONString(generateData));
        String responseBody;
        try {
            String requestBody = XmlUtils.mapToXml(generateData);
            log.info("requestBody:{}", requestBody);
            responseBody = HttpRequest.post(this.unifiedOrderUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                    .body(requestBody)
                    .execute().body();
            log.info("responseBody:{}", responseBody);
            Map<String, String> resultMap = XmlUtils.xmlToMap(responseBody);
            UnifiedOrderResponseDTO unifiedOrderResponseDTO = new UnifiedOrderResponseDTO();
            unifiedOrderResponseDTO.setTimeStamp(String.valueOf(System.currentTimeMillis()));
            BeanUtil.copyProperties(resultMap, unifiedOrderResponseDTO);
            //生成paySign问题
            Map<String, Object> paySignMap = generatePaySignMap(unifiedOrderResponseDTO);
            String paySign = generateSign(paySignMap, paySecretKey);
            unifiedOrderResponseDTO.setSign(paySign);
            log.info("paySign:{}", paySign);
            return unifiedOrderResponseDTO;
        } catch (TransformerException | IOException | ParserConfigurationException | SAXException e) {
            log.error(e.getMessage(), e);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "微信统一下单失败!");
        }
    }

    /**
     * 查询订单接口
     *
     * @param outTradeNo 商户订单号
     */
    public OrderQueryResponseDTO orderQuery(@Nonnull final String outTradeNo) {
        Preconditions.checkNotNull(outTradeNo);

        Map<String, Object> generateData = new HashMap<>();
        generateData.put("out_trade_no", outTradeNo);

        generateData.put("appid", appId);
        generateData.put("mch_id", mchId);

        String nonce_str = IdUtil.fastSimpleUUID();
        generateData.put("nonce_str", nonce_str);
        if (StringUtils.isBlank(sandboxSignUrl)) {
            generateData.put("sign", generateSign(generateData, paySecretKey));
        } else {
            generateData.put("sign", generateSign(generateData, getSandboxSign()));
        }

        log.debug("Generate Data: {}", JSON.toJSONString(generateData));
        String responseBody;
        try {
            responseBody = HttpRequest.post(this.orderQueryUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                    .body(XmlUtils.mapToXml(generateData))
                    .execute().body();
            System.out.println(responseBody);
            Map<String, String> resultMap = XmlUtils.xmlToMap(responseBody);
            Map<String, Object> checkSignMap = new HashMap<>(resultMap);
            if (!this.checkSign(checkSignMap, resultMap.get("sign"))) {
                throw new NJUException(ExceptionEnum.SERVER_ERROR, "支付接口签名失败，存在安全风险!");
            }
            OrderQueryResponseDTO orderQueryResponseDTO = new OrderQueryResponseDTO();
            BeanUtil.copyProperties(resultMap, orderQueryResponseDTO);
            return orderQueryResponseDTO;
        } catch (TransformerException | IOException | ParserConfigurationException | SAXException e) {
            log.error(e.getMessage(), e);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "微信查询订单失败!");
        }
    }

    public String closeOrder(@Nonnull final String outTradeNo) {
        Preconditions.checkNotNull(outTradeNo);

        Map<String, Object> generateData = new HashMap<>();
        generateData.put("out_trade_no", outTradeNo);

        generateData.put("appid", appId);
        generateData.put("mch_id", mchId);

        String nonce_str = IdUtil.fastSimpleUUID();
        generateData.put("nonce_str", nonce_str);
        generateData.put("sign", generateSign(generateData, paySecretKey));

        log.debug("Generate Data: {}", JSON.toJSONString(generateData));
        String responseBody;
        try {
            responseBody = HttpRequest.post(this.closeOrderUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                    .body(XmlUtils.mapToXml(generateData))
                    .execute().body();
            return responseBody;
        } catch (TransformerException | IOException | ParserConfigurationException e) {
            log.error(e.getMessage(), e);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "微信关闭订单失败!");
        }
    }

    public String getSandboxSign() {
        Map<String, Object> generateData = new HashMap<>();
        generateData.put("mch_id", mchId);
        generateData.put("nonce_str", IdUtil.fastSimpleUUID());
        generateData.put("sign", generateSign(generateData, paySecretKey));

        log.debug("Generate data: {}", generateData);
        String responseBody;
        try {
            responseBody = HttpRequest.post(this.sandboxSignUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                    .body(XmlUtils.mapToXml(generateData))
                    .execute().body();
            Map<String, String> resultMap = XmlUtils.xmlToMap(responseBody);
            log.info("resultMap: {}", resultMap);
            SandboxSignResponseDTO sandboxSignResponseDTO = new SandboxSignResponseDTO();
            BeanUtil.copyProperties(resultMap, sandboxSignResponseDTO);
            if (Objects.nonNull(sandboxSignResponseDTO.getReturn_code()) && sandboxSignResponseDTO.getReturn_code().equals("SUCCESS")) {
                return sandboxSignResponseDTO.getSandbox_signkey();
            } else {
                throw new NJUException(ExceptionEnum.SERVER_ERROR, "获取沙箱签名失败!");
            }
        } catch (ParserConfigurationException | TransformerException | IOException | SAXException e) {
            log.error(e.getMessage(), e);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "微信沙箱签名失败!");
        }
    }

    public boolean checkSign(@Nonnull Map<String, Object> originData, String sign) {
        if (sign == null) {
            return false;
        }
        Set<String> keys = new HashSet<>(originData.keySet());
        for (String key : keys) {
            if ("sign".equals(key)) {
                originData.remove(key);
                continue;
            }
            if (Objects.isNull(originData.get(key)) || StringUtils.isBlank(String.valueOf(originData.get(key)))) {
                originData.remove(key);
                continue;
            }
        }
        String realSign;
        if (StringUtils.isBlank(sandboxSignUrl)) {
            realSign = generateSign(originData, paySecretKey);
        } else {
            realSign = generateSign(originData, getSandboxSign());
        }
        return realSign.equals(sign);
    }

    /**
     * 生成签名
     */
    private String generateSign(@Nonnull Map<String, Object> originData, String key) {
        Preconditions.checkNotNull(originData);
        String originStr = mapJoin(originData);
        String stringSignTemp = originStr + "&key=" + key;
        log.info("signStr:{}", stringSignTemp);
        String sign = SecureUtil.md5(stringSignTemp).toUpperCase();
        return sign;
    }

    private String mapJoin(@Nonnull Map<String, Object> map) {
        Preconditions.checkNotNull(map);
        List<String> keyValues = map.keySet().stream()
                .sorted()
                .map(k -> k + "=" + map.get(k))
                .collect(Collectors.toList());
        return StringUtils.join(keyValues, "&");
    }

    private Map<String, Object> generatePaySignMap(UnifiedOrderResponseDTO unifiedOrderResponseDTO) {
        Map<String, Object> map = new HashMap<>();
        map.put("appId", unifiedOrderResponseDTO.getAppid());
        map.put("nonceStr", unifiedOrderResponseDTO.getNonce_str());
        map.put("package", "prepay_id=" + unifiedOrderResponseDTO.getPrepay_id());
        map.put("signType", "MD5");
        map.put("timeStamp", unifiedOrderResponseDTO.getTimeStamp());
        return map;
    }

}
