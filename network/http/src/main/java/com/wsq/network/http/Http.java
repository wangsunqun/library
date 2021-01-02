package com.wsq.network.http;

import com.wsq.util.common.ConfigUtils;

import static com.wsq.network.http.Executor.getExecutor;

public class Http {
    public static final boolean DEFAULT_LOG = ConfigUtils.getBooleanDefault("http.enableLog",true);
    public static final int DEFAULT_RETRIES = ConfigUtils.getIntDefault("http.retries", 0);
    public static final long DEFAULT_TIMEOUT = ConfigUtils.getLongDefault("http.timeout", 60 * 1000L);

    private static final Executor DEFAULT_EXECUTOR = new Executor(DEFAULT_LOG, DEFAULT_RETRIES, DEFAULT_TIMEOUT);

    // 默认开启日志，调用该方法可以关闭http相关日志
    public static Executor disableLog() {
        return getExecutor(false, DEFAULT_RETRIES, DEFAULT_TIMEOUT);
    }

    // 默认不重试，调用该方法可以指定重试次数
    public static Executor retries(int retries) {
        return getExecutor(DEFAULT_LOG, retries, DEFAULT_TIMEOUT);
    }

    // 默认超时时间1分钟，调用该方法可以指定超时时间
    public static Executor timeout(long timeout) {
        return getExecutor(DEFAULT_LOG, DEFAULT_RETRIES, timeout);
    }


}
