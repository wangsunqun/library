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
import java.util.Objects;

/**
 * 获取请求参数
 *
 * @author wsq
 * 2021/1/4 10:19
 */
public class ParamUtils {
    public static String getParams(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        if (MapUtils.isNotEmpty(queryParams)) {
            JSONObject param = new JSONObject();
            queryParams.forEach((k, v) -> param.put(k, v.get(0)));
            return param.toJSONString();
        }

        return null;
    }

    public static String postParams(DataBuffer dataBuffer) {
        if (Objects.isNull(dataBuffer)) return null;

        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
        DataBufferUtils.release(dataBuffer);
        //获取request body
        return charBuffer.toString();
    }
}
