package com.baizhi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * @author jin
 */
public class JWTUtils {


    private static final String SING = "!Q@W3e4r%T^Y";

    /**
     * 生成token  header.payload.sing
     */
    public static String getToken(Map<String, String> map) {

        //默认7天过期
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        //payload
        //lambda表达式：
        //map.forEach(builder::withClaim);
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        //指定令牌过期时间和签名算法
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));
    }

    /**
     * 验证token 合法性
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

//    /**
//     * 获取token信息方法
//     */
//    public static DecodedJWT getTokenInfo(String token){
//        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
//        return verify;
//    }

}
