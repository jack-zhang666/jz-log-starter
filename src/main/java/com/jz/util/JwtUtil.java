package com.jz.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jz.config.JZLogProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Map;

/**
 * @author jack zhang
 * @description: 生成JWT token串与校验token串的工具类
 * @date: 2023/10/18 19:26
 */
public class JwtUtil {

    /**
     * 配置文件中自定义的生成JWT的签名
     */
    private static String SIGNATURE = "";

    @Autowired
    JZLogProperties jzLogProperties;

    @PostConstruct
    private void init() {
        SIGNATURE = jzLogProperties.jwtConfig().getSignature();
    }

    /**
     * 获得token得方法  header.payload.signature
     *
     * @param map 将所有payload封装为一个map
     * @return 返回token
     */
    public static String getToken(Map<String, String> map) {

        //设置过期时间，7天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        String token;
        token = builder
                //指定令牌过期时间
                .withExpiresAt(instance.getTime())
                //密钥signature
                .sign(Algorithm.HMAC256(SIGNATURE));

        return token;
    }

    /**
     * 验证token是否合法的方法
     *
     * @param token 将token当作参数放入，如抛出异常则token不合法，如不抛出异常则token合法
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

    /**
     * 获得token信息的方法
     *
     * @param token 当作参数传入的token
     * @return 返回DecodedJWT对象（JWT解码对象）verify，通过改对象的方法可以获得我们想获得的token里的信息
     */
    public static DecodedJWT getTokenInfo(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
        return verify;
    }
}
