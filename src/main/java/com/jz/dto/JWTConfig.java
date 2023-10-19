package com.jz.dto;

import lombok.Data;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 19:51
 */
@Data
public class JWTConfig {

    /**
     * 生成token的签名
     */
    private String signature;

    /**
     * token中存储用户id的key
     */
    private String tokenKey;
}
