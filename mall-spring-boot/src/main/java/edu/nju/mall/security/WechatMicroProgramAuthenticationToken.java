package edu.nju.mall.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class WechatMicroProgramAuthenticationToken extends AbstractAuthenticationToken {

    private String openid;
    private String sessionKey;
    private String rawData;
    private String signature;

    public WechatMicroProgramAuthenticationToken(String openid, String sessionKey, String rawData, String signature) {
        super(null);
        this.openid = openid;
        this.sessionKey = sessionKey;
        this.rawData = rawData;
        this.signature = signature;
    }

    public WechatMicroProgramAuthenticationToken(String openid, String sessionKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openid = openid;
        this.sessionKey = sessionKey;
    }

    public WechatMicroProgramAuthenticationToken(String openid, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openid = openid;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public Object getCredentials() {
        return this.openid;
    }

    @Override
    public Object getPrincipal() {
        return this.sessionKey;
    }

}
