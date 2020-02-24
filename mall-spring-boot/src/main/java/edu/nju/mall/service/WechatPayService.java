package edu.nju.mall.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private String mapJoin(@Nonnull Map<String, String> map) {
        Preconditions.checkNotNull(map);
        List<String> keyValues = map.keySet().stream()
                .sorted()
                .map(k -> k + "=" + map.get(k))
                .collect(Collectors.toList());
        return StringUtils.join(keyValues, "&");
    }

    private Map<String, String> generateSecureData(Map<String, String> originData) {
        String originStr = mapJoin(originData);
        String stringSignTemp = originStr + "&key=" + paySecretKey;
        String sign = SecureUtil.md5(stringSignTemp).toUpperCase();
        HMac hMac = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, paySecretKey.getBytes());
        sign = hMac.digestHex(sign);
        Map<String, String> results = new HashMap<>();
        results.putAll(originData);
        results.put("sign", sign);
        return results;
    }

}
