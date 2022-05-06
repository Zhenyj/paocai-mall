package com.zyj.paocai.common.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.exception.BizCodeEnum;

import java.util.Date;

/**
 * @author lulx
 * @date 2022-04-10 14:47
 **/
public class JwtUtils {

    /** 默认过期时间7天 */
    public static final long DEFAULT_EXPIRE_TIME = 100 * 60 * 60 * 24 * 7;

    /**
     * 生成token
     *
     * @param member
     * @return
     */
    public static String getToken(MemberRespVo member) {
        Date date = new Date(System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);
        String token = "";
        // 将用户信息保存到token，使用会员id作为token密钥
        token = JWT.create().withAudience(JSON.toJSONString(member))
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(member.getId().toString()));
        return token;
    }

    /**
     * 生成token指定过期时间
     *
     * @param member
     * @param expireTime
     * @return
     */
    public static String getToken(MemberRespVo member, long expireTime) {
        Date date = new Date(System.currentTimeMillis() + expireTime);
        String token = "";
        // 将用户信息保存到token，使用会员id作为token密钥
        token = JWT.create().withAudience(JSON.toJSONString(member))
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(member.getId().toString()));
        return token;
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public static MemberRespVo validateToken(String token) {
        MemberRespVo member = null;
        try {
            // 获取token信息中的用户信息
            member = JSON.parseObject(JWT.decode(token).getAudience().get(0), MemberRespVo.class);
        } catch (JWTDecodeException e) {
            throw new RRException(e.getMessage());
        }
        if (member == null) {
            throw new RRException(BizCodeEnum.PLEASE_LOGIN.getMsg(), BizCodeEnum.PLEASE_LOGIN.getCode());
        }
        // 验证
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(member.getId().toString())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            // 验证失败抛出异常
            throw new RRException(e.getMessage());
        }
        return member;
    }
}
