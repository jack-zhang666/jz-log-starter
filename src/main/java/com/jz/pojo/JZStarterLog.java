package com.jz.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 17:27
 */
@Data
@Entity
@Table(name = "tb_log")
public class JZStarterLog {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tb_log_id", nullable = false)
    private Integer tbLogId;

    /**
     * 日志所属模块
     */
    @Column(name = "tb_log_model", length = 20)
    private String tbLogModel;

    /**
     * 日志级别
     */
    @Column(name = "tb_log_level", nullable = false, length = 10)
    private String tbLogLevel;

    /**
     * 日志类型
     */
    @Column(name = "tb_log_type", nullable = false, length = 20)
    private String tbLogType;

    /**
     * 操作结果,默认0失败，1成功
     */
    @Column(name = "tb_log_record_result", length = 50)
    private Integer tbLogRecordResult;

    /**
     * 用户id
     */
    @Column(name = "tb_log_user_id", length = 50)
    private String tbLogUserId;

    /**
     * 方法
     */
    @Column(name = "tb_log_method", length = 30)
    private String tbLogMethod;

    /**
     * 参数
     */
    @Column(name = "tb_log_method_params", length = 200)
    private String tbLogMethodParams;

    /**
     * 参数值
     */
    @Column(name = "tb_log_method_params_value", length = 100)
    private String tbLogMethodParamsValue;

    /**
     * 开始时间
     */
    @Column(name = "tb_log_method_start_time", length = 50)
    private Date tbLogMethodStartTime;

    /**
     * 结束时间
     */
    @Column(name = "tb_log_method_end_time", length = 50)
    private Date tbLogMethodEndTime;

    /**
     * 异常信息
     */
    @Column(name = "tb_log_error_message", length = 200)
    private String tbLogErrorMessage;

}
