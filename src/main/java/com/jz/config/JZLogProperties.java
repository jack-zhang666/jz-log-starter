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
        JWTConfig jwtConfig = new JWTConfig();
        jwtConfig.setSignature("jz-log-starter");
        return jwtConfig;
    }

    /**
     * 队列
     */
    private String queue = "jz-log-queue";

    /**
     * 交换机
     */
    private String exchange = "jz-log-exchange";

    /**
     * 路由键
     */
    private String routingKey = "jz-log-routingKey";

    /**
     * 请求头中存储uuid的key
     */
    private String uuidKey = "token";

}
