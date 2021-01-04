package com.wsq.network.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wsq.network.http.dto.HttpResponse;
import com.wsq.network.http.executor.Executor;
import com.wsq.util.common.config.ConfigUtils;

import java.util.Map;

import static com.wsq.network.http.executor.Executor.getExecutor;

public class Http {
    private static final boolean DEFAULT_LOG = ConfigUtils.getBooleanDefault("http.enableLog", true);
    private static final long DEFAULT_TIMEOUT = ConfigUtils.getLongDefault("http.timeoutMillis", 60 * 1000L);

    private static final Executor DEFAULT_EXECUTOR = new Executor(DEFAULT_LOG, DEFAULT_TIMEOUT, null);

    // 默认开启日志，调用该方法可以关闭http相关日志
    public static Executor disableLog() {
        return getExecutor(false, DEFAULT_TIMEOUT, null);
    }

    // 默认不重试，调用该方法可以指定重试次数
    public static <T> Executor retry(Class<T> retryStrategy) {
        return getExecutor(DEFAULT_LOG, DEFAULT_TIMEOUT, retryStrategy);
    }

    public static Executor retryDefault(int retries, long retryIntervalMillis) {
        return getExecutor(DEFAULT_LOG, DEFAULT_TIMEOUT, retries, retryIntervalMillis);
    }

    // 默认超时时间1分钟，调用该方法可以指定超时时间
    public static Executor timeout(long timeout) {
        return getExecutor(DEFAULT_LOG, timeout, null);
    }

    public static <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz) {
        return DEFAULT_EXECUTOR.get(url, headers, params, clazz);
    }

    public static <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> params, TypeReference<T> typeReference) {
        return DEFAULT_EXECUTOR.get(url, headers, params, null, typeReference);
    }

    public static <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz, TypeReference<T> typeReference) {
        return DEFAULT_EXECUTOR.get(url, headers, params, clazz);
    }

    public static <T> HttpResponse<T> postForm(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz) {
        return DEFAULT_EXECUTOR.postForm(url, headers, params, clazz, null);
    }

    public static <T> HttpResponse<T> postForm(String url, Map<String, String> headers, Map<String, String> params, TypeReference<T> typeReference) {
        return DEFAULT_EXECUTOR.postForm(url, headers, params, null, typeReference);
    }

    public static <T> HttpResponse<T> postForm(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz, TypeReference<T> typeReference) {
        return DEFAULT_EXECUTOR.postForm(url, headers, params, clazz);
    }

    public static <T> HttpResponse<T> postJson(String url, Map<String, String> headers, Map<String, Object> params, Class<T> clazz) {
        return DEFAULT_EXECUTOR.postJson(url, headers, params, clazz, null);
    }

    public static <T> HttpResponse<T> postJson(String url, Map<String, String> headers, Map<String, Object> params, TypeReference<T> typeReference) {
        return DEFAULT_EXECUTOR.postJson(url, headers, params, null, typeReference);
    }

    public static <T> HttpResponse<T> postJson(String url, Map<String, String> headers, Object params, Class<T> clazz, TypeReference<T> typeReference) {
        return DEFAULT_EXECUTOR.postJson(url, headers, params, clazz, typeReference);
    }

    public static <T> HttpResponse<T> postJson(String url, Map<String, String> headers, String params, Class<T> clazz, TypeReference<T> typeReference) {
        return DEFAULT_EXECUTOR.postJson(url, headers, params, clazz, typeReference);
    }
}
