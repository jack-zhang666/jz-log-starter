package com.jz.config;

import com.jz.dto.JWTConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 19:37
 */
@Data
@ConfigurationProperties(prefix = "jz.log")
public class JZLogProperties {

    @Bean
    public JWTConfig jwtConfig() {
        return new JWTConfig();
    }

    /**
     * 队列
     */
    private String queue;

    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由键
     */
    private String routingKey;

    /**
     * 请求头中存储uuid的key
     */
    private String uuidKey;

    /**
     * token中存储用户id的key
     */
    private String tokenKey;
}
