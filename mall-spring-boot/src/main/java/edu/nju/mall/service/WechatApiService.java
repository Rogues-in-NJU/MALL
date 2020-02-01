package edu.nju.mall.service;

import edu.nju.mall.common.WechatSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(name = "wechat", url = "https://api.weixin.qq.com")
public interface WechatApiService {

    @GetMapping("/sns/jscode2session")
    WechatSession jscode2Session(@RequestParam("appid") final String appId,
                                 @RequestParam("secret") final String appSecret,
                                 @RequestParam("js_code") final String code,
                                 @RequestParam(name = "grant_type", defaultValue = "authorization_code") final String grantType);

}
