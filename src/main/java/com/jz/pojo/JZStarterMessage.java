package com.jz.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/19 10:06
 */
@Data
@Entity
@Table(name = "tb_message")
public class JZStarterMessage implements Serializable {

    /**
     * 主键，消息的业务唯一标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tb_message_id", nullable = false)
    private Integer id;

    /**
     * 消息的状态
     */
    @Column(name = "tb_message_status", nullable = false, length = 10)
    private Integer status;

    /**
     * 消息体
     */
    @Column(name = "tb_message_data", nullable = false, length = 255)
    private String data;

    /**
     * 交换机
     */
    @Column(name = "tb_message_exchange", length = 30)
    private String exchange;

    /**
     * 路由键
     */
    @Column(name = "tb_message_routing_key", length = 30)
    private String routingKey;

    /**
     * 发送消息失败的原因
     */
    @Column(name = "tb_message_cause", length = 200)
    private String cause;
}
