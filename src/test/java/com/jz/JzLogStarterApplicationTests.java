package com.jz;

import com.jz.dto.BaseMessage;
import com.jz.pojo.JZStarterLog;
import com.jz.pojo.JZStarterMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BaseMessage.class, JZStarterMessage.class})
class JzLogStarterApplicationTests {

    @Test
    void contextLoads() {
        BaseMessage baseMessage = new BaseMessage();
        JZStarterLog jzStarterLog = new JZStarterLog();
        jzStarterLog.setTbLogId(1);
        baseMessage.setId("1");
        baseMessage.setStatus(1);
        baseMessage.setCause("1");
        baseMessage.setData(jzStarterLog);
        baseMessage.setExchange("1");
        baseMessage.setRoutingKey("1");

        JZStarterMessage jzStarterMessage = new JZStarterMessage();
        BeanUtils.copyProperties(baseMessage, jzStarterMessage);
        System.out.println(jzStarterMessage);
    }

}
