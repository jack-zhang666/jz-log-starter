package com.jz.constant;

/**
 * @author jack zhang
 * @version 1.0
 * @description: 常量池
 * @date 2023/10/18 16:10
 */
public interface Constant {

    /**
     * 日志级别常量池
     */
    interface LopLevel {

        String DEBUG = "debug";

        String INFO = "info";

        String WARN = "warn";

        String ERROR = "error";
    }

    /**
     * 日志类型常量池
     */
    interface LogType {

        String SELECT = "select";

        String UPDATE = "update";

        String SAVE = "save";

        String DELETE = "delete";
    }

    /**
     * redis key值常量池
     */
    interface RedisKet {

        String LOGIN_KEY = "LOGIN_KEY";

        String LOG_LIST_KEY = "LOG_LIST_KEY";
    }
}
