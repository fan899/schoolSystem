package com.fan.demo.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fan.demo.entity.User;
import com.fan.demo.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName: TokenUtils
 * @Description: 生成token的相关工具类
 * @Author fancy
 * @Date 2022/5/3
 * @Version 1.0
 */
@Component
public class TokenUtils {

    // static 静态变量，也称类变量，因为静态方法仅能调用静态变量
    private static UserService staticUserService;

    // @Resource和@Autowired
    // @Resource不是Spring的注解,但Spring支持该注解，会先照名字注入,再按类型注入
    // @Autowired是Spring提供的注解,按照类型注入,再按名字注入
    @Resource
    private UserService userService;

    // @PostConstruct是Java自带的注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }


    /**
     * 以用户的身份证号和密码生成token
     * @param cardId
     * @param sign
     * @return
     */
    public static String genToken(String cardId, String sign) {
        return JWT.create().withAudience(cardId) // 以用户的身份证号保存到token里面，作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) //设置token2小时后过期
                .sign(Algorithm.HMAC256(sign)); // 以password作为token的密钥
    }

    public static User getCurrentUser() {
        try {
            // 利用RequestContextHolder获取request对象
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // 获取请求头中的token值
            String token = request.getHeader("token");
            // 判断里面的值是否为空
            if (StrUtil.isNotBlank(token)) {
                // 获取里面的用户身份证号
                String cardId = JWT.decode(token).getAudience().get(0);
                // 调用方法查找用户信息
                return staticUserService.getUserByCardId(cardId);
            }
        } catch (JWTDecodeException e) {
            return null;
        }
        return null;
    }
}
