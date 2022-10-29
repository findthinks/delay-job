package com.findthinks.delay.job.console.core.utils;

import com.findthinks.delay.job.console.web.rr.AccountInfo;
import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.lib.utils.UUIDUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationUtils {

    /** token过期时间60分钟 */
    private static final long TOKEN_EXPIRED_TIME = 60 * 60 * 1000;

    private static final String JWT_SECRET = "*jdk&gJw%tE#ncry^pt1.8";

    public static String createJWT(AccountInfo accountInfo) {
        return createJwt(accountInfo, TOKEN_EXPIRED_TIME);
    }

    public static String createJwt(AccountInfo accountInfo, Long time) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();

        Map<String, Object> claims = new HashMap<>();
        claims.put("account", accountInfo.getAccount());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUIDUtils.randomUUID())
                .setIssuedAt(now)
                .signWith(algorithm, secretKey);
        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static Claims parseJwt(String jwt) {
        SecretKey key = generalKey();
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            throw new DelayJobException(ExceptionEnum.AUTHENTICATION_NOT_EXIST, "认证信息无效，请重新登录");
        }
        return claims;
    }

    public static SecretKey generalKey() {
        String stringKey = JWT_SECRET;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}