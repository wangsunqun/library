package com.wsq.library.gateway.starter.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static com.wsq.library.gateway.starter.filter.LogFilter.POST_BODY_PARAM;

/**
 * 获取请求参数
 *
 * @author wsq
 * 2021/1/4 10:19
 */
public class ParamUtils {
    public static String getParams(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();

        StringBuilder params = new StringBuilder();
        if (method == HttpMethod.GET) {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            if (MapUtils.isNotEmpty(queryParams)) {
                JSONObject param = new JSONObject();
                queryParams.forEach((k, v) -> param.put(k, v.get(0)));
                params.append(param.toJSONString());
            }
        } else {
            DataBuffer dataBuffer = exchange.getAttribute(POST_BODY_PARAM);
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
            DataBufferUtils.release(dataBuffer);
            //获取request body
            params.append(charBuffer);
        }

        return params.toString();
    }
}
