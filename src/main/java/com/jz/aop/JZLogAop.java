package com.jz.aop;

import cn.hutool.core.util.IdUtil;
import com.jz.annotation.JZLog;
import com.jz.config.JZLogProperties;
import com.jz.constant.Constant;
import com.jz.dto.BaseMessage;
import com.jz.pojo.JZStarterLog;
import com.jz.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 16:07
 */
@Component
@Aspect
@Slf4j
public class JZLogAop {

    /**
     * 将JZLog注解标注的类或方法当作切点
     */
    @Pointcut(value = "@annotation(com.jz.annotation.JZLog)")
    public void pt1() {
    }

    @Autowired
    JZLogProperties jzLogProperties;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Around(value = "pt1()")
    public Object recordLog(ProceedingJoinPoint proceedingJoinPoint) {

        JZStarterLog jzStarterLog = new JZStarterLog();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String uuid = request.getHeader(jzLogProperties.getUuidKey());
        if (!StringUtils.isEmpty(uuid)) {
            String token = stringRedisTemplate.opsForValue().get(Constant.RedisKet.LOGIN_KEY + uuid);
            String userId = JwtUtil.getTokenInfo(token).getClaim(jzLogProperties.getTokenKey()).asString();
            if (!StringUtils.isEmpty(userId)) {
                jzStarterLog.setTbLogUserId(userId);
            }
        }

        Object[] args = proceedingJoinPoint.getArgs();
        StringBuffer s = new StringBuffer();
        for (Object arg : args) {
            s.append(arg).append(",");
        }
        jzStarterLog.setTbLogMethodParamsValue(s.toString());

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        jzStarterLog.setTbLogMethod(method.getName());

        Parameter[] parameters = method.getParameters();
        StringBuffer s1 = new StringBuffer();
        for (Parameter parameter : parameters) {
            s1.append(parameter).append(",");
        }
        jzStarterLog.setTbLogMethodParams(s1.toString());

        Object result = null;
        if (method.isAnnotationPresent(JZLog.class)) {
            JZLog jzLog = method.getAnnotation(JZLog.class);
            String model = jzLog.model();
            jzStarterLog.setTbLogModel(model);

            String logLevel = jzLog.LogLevel();
            jzStarterLog.setTbLogLevel(logLevel);

            String type = jzLog.type();
            jzStarterLog.setTbLogType(type);

            boolean b = jzLog.recordResult();
            if (b) {
                try {
                    jzStarterLog.setTbLogRecordResult(1);
                    jzStarterLog.setTbLogMethodStartTime(new Date());
                    result = proceedingJoinPoint.proceed();
                } catch (Throwable e) {
                    e.printStackTrace();
                    jzStarterLog.setTbLogRecordResult(0);
                    jzStarterLog.setTbLogErrorMessage(e.getMessage());
                }
            } else {
                try {
                    jzStarterLog.setTbLogMethodStartTime(new Date());
                    result = proceedingJoinPoint.proceed();
                } catch (Throwable e) {
                    e.printStackTrace();
                    jzStarterLog.setTbLogErrorMessage(e.getMessage());
                }
            }

            jzStarterLog.setTbLogMethodEndTime(new Date());

            String id = IdUtil.getSnowflakeNextIdStr();
            BaseMessage baseMessage = new BaseMessage();
            baseMessage.setData(jzStarterLog);
            baseMessage.setId(id);
            baseMessage.setExchange(jzLogProperties.getExchange());
            baseMessage.setRoutingKey(jzLogProperties.getRoutingKey());
            baseMessage.setStatus(2);

            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(id);
            /**
             * 发送消息到rabbitmq
             */
            rabbitTemplate.convertAndSend(jzLogProperties.getExchange(), jzLogProperties.getRoutingKey(), baseMessage, correlationData);

            /**
             * 将该条消息往redis中放一份
             */
            redisTemplate.opsForList().rightPush(Constant.RedisKet.LOG_LIST_KEY, baseMessage);
        }
        return result;
    }
}
