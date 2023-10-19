package com.jz.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 20:00
 */
@Slf4j
@EnableScheduling
@EnableConfigurationProperties(JZLogProperties.class)
public class JZLogAutoConfig {

    @Resource
    JZLogProperties jzLogProperties;

    /**
     * 创建queue对象
     */
    @Bean(name = "JZLogQueue")
    public Queue logQueue() {
        /**
         * params1:队列名
         * params2:服务器重启后队列是否存活
         * params3:是否是独占队列
         * params4:是否自动删除
         */
        return new Queue(jzLogProperties.getQueue(), true, false, false);
    }

    /**
     * 创建exchange对象
     */
    @Bean(name = "JZLogExchange")
    public TopicExchange topicExchange() {
        /**
         * params1:交换机名
         * params2:服务器重启后交换机是否存活
         * params3:是否自动删除
         */
        return new TopicExchange(jzLogProperties.getExchange(), true, false);
    }

    /**
     * 将queue与exchange对象绑定
     */
    @Bean
    public Binding binding(@Qualifier(value = "JZLogQueue") Queue queue,
                           @Qualifier(value = "JZLogExchange") TopicExchange topicExchange) {
        return BindingBuilder
                .bind(queue)
                .to(topicExchange)
                .with(jzLogProperties.getRoutingKey());
    }

    @Resource
    ConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("CorrelationData({}) ack failed: {}", correlationData, cause);
            }
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("send message({}) to exchange({}) failed: {}, routingKey({}), replyCode({})", message, exchange, replyText, routingKey, replyCode);
        });

        return rabbitTemplate;
    }

}
