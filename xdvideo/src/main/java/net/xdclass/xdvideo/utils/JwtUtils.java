package net.xdclass.xdvideo.utils;

import io.jsonwebtoken.*;
import net.xdclass.xdvideo.domain.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * jwt token工具类
 */
public class JwtUtils {
    /**
     * 发行者
     */
    public static final String SUBJECT = "xdclass";
    /**
     * jwt token过期时间，单位ms,这里设置一天
     */
    private static final long EPIRE_TIME_MILLSECONDS = 24 * 60 * 60 * 1000;
    /**
     * JWT TOKEN签名密钥  实际使用时应该是加密过的密钥
     */
    private static final String APPSECRET = "xd168";

    /**
     * 生成jwt token
     * @param user 用户非敏感信息
     * @return
     */
    public static String generateJsonWebToken(User user) {

        if(userInfoIsNull(user)){
            return  StringUtils.EMPTY;
        }

        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("img", user.getHeadImg())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EPIRE_TIME_MILLSECONDS))
                .signWith(SignatureAlgorithm.HS256, APPSECRET)
                .compact();
        return token;
    }

    /**
     * 服务端校验 token是否有效
     * @param token
     * @return
     */
    public static Claims checkToken(String token){
        //getBody()返回jwt token中的payload部分

        final  Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
        }

        return null;
    }




    private static boolean userInfoIsNull(User user){
        return (user == null || user.getId() == null || user.getName() == null || user.getHeadImg() == null);
    }
}
