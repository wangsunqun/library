package com.wsq.library.network.http.executor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.wsq.library.network.http.dto.HttpResponse;
import com.wsq.library.network.http.interceptor.DefaultRetryInterceptor;
import com.wsq.library.util.common.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.wsq.library.util.common.response.ResponseStatusEnums.INTERNAL_SERVER_ERROR;

/**
 * http执行器
 *
 * @author wsq
 * 2020/12/31 17:02
 */
@Slf4j
public class Executor extends OkHttpRequester {
    public <T> Executor(boolean log, long timeout, Class<T> retryStrategy) {
        super(log, timeout, retryStrategy);
    }

    /**
     * 三个维度确定独立的executor，确保不相互影响
     */
    private static final Map<Boolean, Map<Long, Executor>> EXECUTOR_MAP = Maps.newConcurrentMap();

    public static <T> Executor getExecutor(boolean log, long timeout, Class<T> retryStrategy) {
        Map<Long, Executor> timeoutMap = EXECUTOR_MAP.computeIfAbsent(log, k -> Maps.newConcurrentMap());
        return timeoutMap.computeIfAbsent(timeout, k -> new Executor(log, timeout, retryStrategy));
    }

    public static Executor getExecutor(boolean log, long timeout, int retries, long retryIntervalMillis) {
        Executor executor = getExecutor(log, timeout, DefaultRetryInterceptor.class);
        executor.setDefault_retry_strategy_retries(retries);
        executor.setDefault_retry_strategy_retryIntervalMillis(retryIntervalMillis);
        return executor;
    }

    // 默认开启日志，调用该方法可以关闭http相关日志
    public Executor disableLog() {
        return getExecutor(false, timeoutMillis, null);
    }

    // 默认不重试，调用该方法可以指定重试次数
    public <T> Executor retry(Class<T> retryStrategy) {
        return getExecutor(enableLog, timeoutMillis, retryStrategy);
    }

    public Executor retryDefault(int retries, long retryIntervalMillis) {
        return getExecutor(enableLog, timeoutMillis, retries, retryIntervalMillis);
    }

    // 默认超时时间1分钟，调用该方法可以指定超时时间
    public Executor timeout(long timeout) {
        return getExecutor(enableLog, timeout, null);
    }

    public <T> HttpResponse<T> get(String url, Class<T> clazz) {
        return get(url, null, null, clazz, null);
    }

    public <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz) {
        return get(url, headers, params, clazz, null);
    }

    public <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> params, TypeReference<T> typeReference) {
        return get(url, headers, params, null, typeReference);
    }

    public <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(url)) {
            return new HttpResponse<>(-1, null, null, "url为空");
        }

        Request.Builder request = new Request.Builder();

        // 添加headers
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(request::addHeader);
        }

        // 添加params
        if (!Objects.isNull(params)) {
            HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
            params.forEach(builder::addQueryParameter);
            url = builder.build().toString();
        }

        // 添加url
        request.url(url);

        HttpResponse<T> steamResponse = exec(request, clazz, typeReference);

        log.info("请求地址:{}, 耗时:{}, 返回码:{}, 返回信息:{}",
                url, steamResponse.getExecTime(), steamResponse.getHttpStatus(),
                steamResponse.getHttpStatus() == 200 ? JsonUtils.to(steamResponse.getBody()) : steamResponse.getErrMsg());

        return steamResponse;
    }

    public <T> HttpResponse<T> postForm(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz) {
        return postForm(url, headers, params, clazz, null);
    }

    public <T> HttpResponse<T> postForm(String url, Map<String, String> headers, Map<String, String> params, TypeReference<T> typeReference) {
        return postForm(url, headers, params, null, typeReference);
    }

    public <T> HttpResponse<T> postForm(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(url)) {
            return new HttpResponse<>(-1, null, null, "url为空");
        }

        Request.Builder request = new Request.Builder();

        // 添加url
        request.url(url);

        // 添加headers
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(request::addHeader);
        }

        // 添加params
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach(bodyBuilder::add);
        }

        request.post(bodyBuilder.build());

        HttpResponse<T> steamResponse = exec(request, clazz, typeReference);

        log.info("请求地址:{}, 参数:{}, 耗时:{}, 返回码:{}, 返回信息:{}",
                url, JsonUtils.to(params), steamResponse.getExecTime(), steamResponse.getHttpStatus(),
                steamResponse.getHttpStatus() == 200 ? JsonUtils.to(steamResponse.getBody()) : steamResponse.getErrMsg());

        return steamResponse;
    }

    public <T> HttpResponse<T> postJson(String url, Map<String, String> headers, Map<String, Object> params, Class<T> clazz) {
        return postJson(url, headers, params, clazz, null);
    }

    public <T> HttpResponse<T> postJson(String url, Map<String, String> headers, Map<String, Object> params, TypeReference<T> typeReference) {
        return postJson(url, headers, params, null, typeReference);
    }

    public <T> HttpResponse<T> postJson(String url, Map<String, String> headers, Object params, Class<T> clazz, TypeReference<T> typeReference) {
        return postJson(url, headers, params == null ? JsonUtils.to(new HashMap<>()) : JsonUtils.to(params), clazz, typeReference);
    }

    public <T> HttpResponse<T> postJson(String url, Map<String, String> headers, String params, Class<T> clazz, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(url)) {
            return new HttpResponse<>(-1, null, null, "url为空");
        }

        Request.Builder request = new Request.Builder();

        // 添加url
        request.url(url);

        // 添加headers
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(request::addHeader);
        }

        // 添加params
        if (StringUtils.isNotBlank(params)) {
            request.post(RequestBody.create(MediaType.get("application/json"), params));
        }

        HttpResponse<T> steamResponse = exec(request, clazz, typeReference);

        log.info("请求地址:{}, 参数:{}, 耗时:{}, 返回码:{}, 返回信息:{}",
                url, JsonUtils.to(params), steamResponse.getExecTime(), steamResponse.getHttpStatus(),
                steamResponse.getHttpStatus() == 200 ? JsonUtils.to(steamResponse.getBody()) : steamResponse.getErrMsg());

        return steamResponse;
    }

    private <T> HttpResponse<T> exec(Request.Builder request, Class<T> clazz, TypeReference<T> typeReference) {
        Call call = httpClient.newCall(request.build());

        HttpResponse<T> steamResponse;
        try {
            long startTime = Clock.systemUTC().millis();
            Response response = call.execute();
            long execTime = Clock.systemUTC().millis() - startTime;

            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                T result = null;
                if (!Objects.isNull(body)) {
                    String bodyString = body.string();
                    if (clazz != null) {
                        result = JsonUtils.from(bodyString, clazz);
                    } else {
                        result = JsonUtils.from(bodyString, typeReference.getType());
                    }
                }
                steamResponse = new HttpResponse<>(response.code(), result, execTime, null);
            } else {
                steamResponse = new HttpResponse<>(response.code(), null, execTime, response.message());
            }
        } catch (Exception e) {
            steamResponse = new HttpResponse<>(INTERNAL_SERVER_ERROR.code, null, null, e.getMessage());
        }

        return steamResponse;
    }
}
