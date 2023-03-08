package cn.postwall.blog.utils;

import cn.postwall.blog.pojo.constant.SysConstant;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Component
public class JwtHelper {

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);


    /**
     * key（按照签名算法的字节长度设置key）
     */
    private static String SECRET_KEY = "abcdefghjk_0123456789_9876543210";
    /**
     * 过期时间（单位毫秒）
     */
    private static long TOKEN_EXPIRE_MILLIS = 86400000;

    /**
     * 创建token
     * @param claimMap
     * @return
     */
    public static String createToken(Map<String, Object> claimMap) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(currentTimeMillis))    // 设置签发时间
                .setExpiration(new Date(currentTimeMillis + TOKEN_EXPIRE_MILLIS))   // 设置过期时间
                .addClaims(claimMap)
                .signWith(generateKey())
                .compact();
    }

    /**
     * 验证token
     * @param token
     * @return 0 验证成功，1、2、3、4、5 验证失败
     */
    public static int verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey(generateKey()).parseClaimsJws(token);
            return 0;
        } catch (ExpiredJwtException e) {
            logger.error("token已过期：{}", token);
            return 1;
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的token：{}", token);
            return 2;
        } catch (MalformedJwtException e) {
            logger.error("token格式不正确：{}", token);
            return 3;
        } catch (SignatureException e) {
            logger.error("token签名不正确：{}", token);
            return 4;
        } catch (IllegalArgumentException e) {
            logger.error("非法参数异常：{}", e);
            return 5;
        }
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static Map<String, Object> parseToken(String token) {
        return Jwts.parser()  // 得到DefaultJwtParser
                .setSigningKey(generateKey()) // 设置签名密钥
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成安全密钥
     * @return
     */
    public static Key generateKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 获取当前token的用户id
     */
    public static long getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader(SysConstant.AUTHORIZATION);
        if (token == null || "".equals(token)) {
            return -1L;
        }
        // 解析Token
        Map<String, Object> map;
        try{
            map = JwtHelper.parseToken(token);
        }catch (Exception e){
            logger.error("获取当前操作用户id失败：", e);
            return -1L;
        }
        return Long.parseLong(map.get("id").toString());
    }

}
