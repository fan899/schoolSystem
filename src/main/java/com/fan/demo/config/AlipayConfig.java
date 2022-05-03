package com.fan.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName: AlipayConfig
 * @Description: 阿里沙箱支付的配置类
 * @Author fancy
 * @Date 2022/5/3
 * @Version 1.0
 */
@Data
@Component
// @ConfigurationProperties是读取配置文件的注解
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {

    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;


}
