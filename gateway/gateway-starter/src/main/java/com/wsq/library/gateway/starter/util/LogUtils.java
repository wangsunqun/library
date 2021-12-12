package com.wsq.library.gateway.starter.util;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.time.Clock;

@Data
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
}
