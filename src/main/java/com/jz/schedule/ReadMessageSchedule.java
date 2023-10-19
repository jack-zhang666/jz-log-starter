package com.jz.schedule;

import com.jz.constant.Constant;
import com.jz.repository.JZMessageRepository;
import com.jz.dto.BaseMessage;
import com.jz.pojo.JZStarterMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/19 8:51
 */
@Component
public class ReadMessageSchedule {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JZMessageRepository jzMessageRepository;

    @Scheduled(cron = "0 0/30 * * * ? ")
    public void checkLogMessage() {

        /**
         * 从redis中取出消息集合
         */
        List<BaseMessage> list = redisTemplate.opsForList().range(Constant.RedisKet.LOG_LIST_KEY, 0, -1);
        list.stream().forEach(baseMessage -> {
            if (!(baseMessage.getStatus() == 3) && !StringUtils.isEmpty(baseMessage.getCause())) {
                JZStarterMessage jzStarterMessage = new JZStarterMessage();
                BeanUtils.copyProperties(baseMessage, jzStarterMessage);
                jzStarterMessage.setData(baseMessage.getData().toString());

                jzMessageRepository.save(jzStarterMessage);
            }
        });
    }
}
