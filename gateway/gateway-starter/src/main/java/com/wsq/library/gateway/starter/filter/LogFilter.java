package com.wsq.library.gateway.starter.filter;

import com.wsq.library.gateway.starter.util.IpUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wsq
 * @Description
 * @date 2021/12/1 19:41
 */
public class LogFilter implements WebFilter {
    public static final String HEADERS = "headers";
    public static final String POST_BODY_PARAM = "postBodyParam";
    private static final String START_TIME_MIL = "startTime";
    private static final String CLIENT_IP = "clientIp";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        setHeader(exchange, CLIENT_IP, IpUtils.getIpAddress(exchange.getRequest()));

        exchange.getAttributes().put(START_TIME_MIL, Clock.systemUTC().millis());
//        chain.filter(exchange)

        return null;
    }

    private void setHeader(ServerWebExchange exchange, String key, String value) {
        Map<String, String> headers = (Map<String, String>) exchange.getAttributes().computeIfAbsent(HEADERS, k -> new HashMap<String, String>());
        headers.put(key, value);
    }

    public static class PartnerServerHttpRequestDecorator extends ServerHttpRequestDecorator {
        private Flux<DataBuffer> body;

        public PartnerServerHttpRequestDecorator(ServerHttpRequest delegate) {
            super(delegate);

            String uri = delegate.getURI().toString();
            HttpMethod method = delegate.getMethod();

            Flux<DataBuffer> flux = super.getBody();

        }

        @Override
        public Flux<DataBuffer> getBody() {
            return body;
        }
    }

    public static void main(String[] args) {
        Flux.just(Flux.just(new Integer[]{1, 11}, new Integer[]{2, 22})).flatMap(s -> s.).
                subscribe(e -> System.out.print(e));

//        Flux.just(new Integer[]{1, 11}, new Integer[]{2, 22}).flatMap(s -> Flux.fromArray(s)).
//                subscribe(e -> System.out.print(e));

    }
}