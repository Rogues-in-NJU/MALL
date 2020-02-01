package edu.nju.mall.security;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSON;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.Role;
import edu.nju.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hermc
 */
@Component
@Slf4j
public class WechatMicroProgramAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatMicroProgramAuthenticationToken token = (WechatMicroProgramAuthenticationToken) authentication;
        UserDTO userDTO = userService.findUser(token.getOpenid());
        if (Objects.isNull(userDTO)) {
            // 需要注册用户信息
            log.debug("user account not exist, begin to register. openid is [{}]", token.getOpenid());
            // 微信签名校验
            Digester digester = new Digester(DigestAlgorithm.SHA1);
            String data = token.getRawData() + token.getSessionKey();
            String signature = digester.digestHex(data);
            if (!signature.equals(token.getSignature())) {
                log.error("signature is invalid, got: [{}] vs cal: [{}]", token.getSignature(), signature);
                throw ExceptionEnum.ILLEGAL_USER.exception("用户签名不正确，请确认账号安全!");
            }
            // userDTO = new UserDTO();
            userDTO = JSON.parseObject(token.getRawData(), UserDTO.class);
            userDTO.setOpenid(token.getOpenid());
            userDTO.setSessionKey(token.getSessionKey());
            log.info("user account is [{}]", userDTO);
            userDTO = userService.register(userDTO);
        }
        List<Role> roles = userService.findUserRoles(userDTO.getId());
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new WechatMicroProgramAuthenticationToken(userDTO.getOpenid(), userDTO.getSessionKey(), authorities);
    }

}
