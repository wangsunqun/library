package com.wsq.library.gateway.plugin.test.dto;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体类，兼缓存
 * @author wsq
 * @date 2021/12/14 14:38
 */
@Data
public class TestRuleDto {
    public static final Map<String, TestRuleDto> CACHE = new ConcurrentHashMap<>();

    private int flag;
    private String value;
}
