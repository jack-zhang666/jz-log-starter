package com.jz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/19 0:37
 */
@Data
public class BaseMessage implements Serializable {

    /**
     * 消息的业务唯一标识
     */
    private String id;

    /**
     * 0:发送到交换机失败
     * 1：发送到队列失败
     * 2：消费失败
     * 3：消费成功
     */
    private Integer status;

    /**
     * 消息体
     */
    private Object data;

    /**
     * 交换机的名字
     */
    private String exchange;

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 失败的原因
     */
    private String cause;
}
