package com.chatroom.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;


public class TokenUtil {

    private static final String key = "jweijo1231ojiSDJOIA23Ssdii";// 密钥签名

    private static final Integer end_day = 7;// 密钥过期天数


    public static String signToken(String username) {
        HashMap<String, Object> map = new HashMap<>();

        // 指定令牌的过期时间，这里是【7天】后令牌token失效
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_WEEK, end_day);


        return JWT.create()
                .withHeader(map) // header可以不写，因为默认值就是它
                .withClaim("user", username)  //payload
                .withExpiresAt(instance.getTime()) // 指定令牌的过期时间
                .sign(Algorithm.HMAC256(key));
    }

    //验证token
    public static boolean verify(String token) {
        try{
            JWT.require(Algorithm.HMAC256(key)).build().verify(token);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    //获取token内容
    public static String getTokenInfo(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(key)).build().verify(token);
        return verify.getClaim("user").asString();
    }

}
