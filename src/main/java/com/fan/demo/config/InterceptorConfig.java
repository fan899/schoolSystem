package com.fan.demo.config;

import com.fan.demo.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: InterceptorConfig
 * @Description: 配置拦截器
 * @Author fancy
 * @Date 2022/5/3
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**") // 拦截全部请求，通过判断token是否合法来决定是否已登录
                .excludePathPatterns("/user/login", "/user/register", "/**/export", "/**/import", "/alipay/**", "/file/**")
                .excludePathPatterns("/swagger**/**", "/webjars/**", "/v3/**", "/doc.html"); // 放行相关接口
    }

    // 让jwtInterceptor注入到容器里面
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
