package com.wsq.network.http;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

import static com.wsq.network.http.Http.*;

/**
 * http执行器
 *
 * @author wsq
 * 2020/12/31 17:02
 */
@Data
@AllArgsConstructor
public class Executor {
    private boolean log;
    private int retries;
    private long timeout;

    /**
     * 三个维度确定独立的executor，确保不相互影响
     */
    private static final Map<Boolean, Map<Integer, Map<Long, Executor>>> EXECUTOR_MAP = Maps.newConcurrentMap();
    public static Executor getExecutor(boolean log, int retries, long timeout) {
        Map<Integer, Map<Long, Executor>> retriesMap = EXECUTOR_MAP.computeIfAbsent(log, k -> Maps.newConcurrentMap());
        Map<Long, Executor> timeoutMap = retriesMap.computeIfAbsent(retries, k -> Maps.newConcurrentMap());
        return timeoutMap.computeIfAbsent(timeout, k -> new Executor(log, retries, timeout));
    }

    // 默认开启日志，调用该方法可以关闭http相关日志
    public Executor disableLog() {
        return getExecutor(false, DEFAULT_RETRIES, DEFAULT_TIMEOUT);
    }

    // 默认不重试，调用该方法可以指定重试次数
    public Executor retries(int retries) {
        return getExecutor(DEFAULT_LOG, retries, DEFAULT_TIMEOUT);
    }

    // 默认超时时间1分钟，调用该方法可以指定超时时间
    public Executor timeout(long timeout) {
        return getExecutor(DEFAULT_LOG, DEFAULT_RETRIES, timeout);
    }
}
