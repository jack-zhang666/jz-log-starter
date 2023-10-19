package com.jz.annotation;

import com.jz.constant.Constant;

import java.lang.annotation.*;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 16:06
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JZLog {

    /**
     * 日志所属类型，默认select
     *
     * @return
     */
    String type() default Constant.LogType.SELECT;

    /**
     * 日志所属模块
     *
     * @return
     */
    String model() default "测试模块";

    /**
     * 日志级别，默认info
     *
     * @return
     */
    String LogLevel() default Constant.LopLevel.INFO;

    /**
     * 是否记录该次操作结果，默认false
     *
     * @return
     */
    boolean recordResult() default false;
}
