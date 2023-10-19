package com.jz.listen;

import com.jz.config.JZLogProperties;
import com.jz.constant.Constant;
import com.jz.repository.JZLogRepository;
import com.jz.dto.BaseMessage;
import com.jz.pojo.JZStarterLog;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 23:58
 */
@Component
public class JZLogListen {

    @Autowired
    JZLogRepository jzLogRepository;

    @Autowired
    JZLogProperties jzLogProperties;

    @Autowired
    RedisTemplate redisTemplate;

    @RabbitListener(queues = "jz-log-queue")
    public void listenLogMessage(Message message, Channel channel, BaseMessage baseMessage) {

        Object data = baseMessage.getData();
        JZStarterLog jzStarterLog = (JZStarterLog) data;
        try {
            /**
             * 将日志对象存入mysql
             */
            JZStarterLog save = jzLogRepository.save(jzStarterLog);
            if (!StringUtils.isEmpty(save)) {
                baseMessage.setStatus(3);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            baseMessage.setStatus(2);
            baseMessage.setCause(e.getMessage());
        }
        redisTemplate.opsForList().rightPush(Constant.RedisKet.LOG_LIST_KEY, baseMessage);
    }
}
