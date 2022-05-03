package com.fan.demo.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fan.demo.common.Constants;
import com.fan.demo.entity.User;
import com.fan.demo.exception.ServiceException;
import com.fan.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: JwtInterceptor
 * @Description: token的拦截器
 * @Author fancy
 * @Date 2022/5/3
 * @Version 1.0
 */


public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 取出请求头中的token
        String token = request.getHeader("token");

        // 不是映射到方法的请求就直接跳过
        if (! (handler instanceof HandlerMethod)) {
            return true;
        }

        // 执行认证，当token为空时，不允许访问
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_401, "token值为空，请重新登录");
        }

        // 获取token中的user cardId
        String cardId;
        try {
            // 解码载荷的内容
            cardId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException(Constants.CODE_401,"token验证异常，请重新登录");
        }

        // 根据解码出来的cardId查找用户
        User user = userService.getUserByCardId(cardId);
        if (user == null) {
            throw new ServiceException(Constants.CODE_401,"用户不存在，请重新登录");
        }

        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();

        try {
            // 验证token
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证异常，请重新登录");
        }

        return true;
    }
}
