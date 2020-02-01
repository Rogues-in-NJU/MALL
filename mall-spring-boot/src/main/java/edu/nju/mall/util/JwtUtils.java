package edu.nju.mall.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hermc
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private static final String CLAIM = "info";

    /**
     * 生成token
     *
     * @param info 存储信息
     * @return token字符串
     */
    public String createToken(String info) {
        Date date = new Date(System.currentTimeMillis() + expiration);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withClaim(CLAIM, info)
                .withExpiresAt(date)
                .sign(algorithm);
        return tokenHead + token;
    }

    /**
     * 校验token是否正确
     *
     * @param token token字符串
     * @param info  存储信息
     * @return true token正确，false token不正确
     */
    public boolean verify(String token, String info) {
        if (token == null || token.length() <= tokenHead.length()) {
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(CLAIM, info)
                    .build();
            DecodedJWT jwt = verifier.verify(this.cutHead(token));
            return true;
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取token中的信息，无需secret解密也能获得
     *
     * @param token token字符串
     * @return 存储信息
     */
    public String getInfo(String token) {
        if (token == null || token.length() <= tokenHead.length()) {
            return null;
        }
        try {
            DecodedJWT jwt = JWT.decode(this.cutHead(token));
            return jwt.getClaim(CLAIM).asString();
        } catch (JWTDecodeException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 校验时间是否超时
     *
     * @param token token字符串
     * @return true超时，false未超时
     */
    public boolean isTimeout(String token) {
        if (token == null || token.length() <= tokenHead.length()) {
            return true;
        }
        try {
            DecodedJWT jwt = JWT.decode(this.cutHead(token));
            Date expiration = jwt.getExpiresAt();
            Date now = new Date();
            return now.after(expiration);
        } catch (JWTDecodeException e) {
            log.error(e.getMessage());
            return true;
        }
    }

    /**
     * 减去token头部部分
     *
     * @param token token字符串
     * @return 减去后的部分
     */
    private String cutHead(String token) {
        return token.substring(tokenHead.length());
    }

}
