package com.wsq.library.gateway.starter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.time.Clock;

public class LogUtils {
    private static final Logger log = LoggerFactory.getLogger(LogUtils.class.getName());

    private final long startTime;
    private String path;
    private String method;
    private String requestIp;
    private String params;
    private String response;

    public LogUtils(ServerHttpRequest request) {
        this.startTime = Clock.systemUTC().millis();
        this.path = request.getURI().getPath();
        this.method = request.getMethodValue();
        this.requestIp = IpUtils.getIpAddress(request);
    }

    public void log() {
        log.info("path: {}, method: {}, requestIp: {}, params: {}, response: {}, costTime: {}ms",
                path, method, requestIp, params, response, Clock.systemUTC().millis() - startTime);
    }

    public static Logger getLog() {
        return log;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
