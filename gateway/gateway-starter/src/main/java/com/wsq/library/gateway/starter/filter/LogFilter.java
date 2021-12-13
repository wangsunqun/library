package com.wsq.library.gateway.starter.filter;

import com.wsq.library.gateway.starter.util.LogUtils;
import com.wsq.library.gateway.starter.util.ParamUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author wsq
 * @Description
 * @date 2021/12/1 19:41
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter implements WebFilter {
    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        LogUtils logData = new LogUtils(request);

        return chain.filter(exchange.mutate().
                        request(new PartnerServerHttpRequestDecorator(exchange, logData)).
                        response(new PartnerServerHttpResponseDecorator(exchange, logData)).
                        build()).
                then(Mono.fromRunnable(logData::log));
    }

    public static class PartnerServerHttpRequestDecorator extends ServerHttpRequestDecorator {
        private final ServerWebExchange exchange;
        private final LogUtils logData;

        public PartnerServerHttpRequestDecorator(ServerWebExchange exchange, LogUtils logData) {
            super(exchange.getRequest());
            this.exchange = exchange;
            this.logData = logData;
            logData.setParams(ParamUtils.getParams(exchange));
        }

        @NonNull
        @Override
        public Flux<DataBuffer> getBody() {
            return exchange.getRequest().getBody().map(dataBuffer -> {
                DataBufferUtils.retain(dataBuffer);
                logData.setParams(ParamUtils.postParams(dataBuffer));
                return dataBuffer;
            });
        }
    }

    public static class PartnerServerHttpResponseDecorator extends ServerHttpResponseDecorator {
        private final ServerWebExchange exchange;
        private final LogUtils logData;

        public PartnerServerHttpResponseDecorator(ServerWebExchange exchange, LogUtils logData) {
            super(exchange.getResponse());
            this.exchange = exchange;
            this.logData = logData;
        }

        @NonNull
        @Override
        public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
            return super.writeWith(DataBufferUtils.join(body).map(dataBuffer -> {
                byte[] content = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(content);
                DataBufferUtils.release(dataBuffer);
                String result = new String(content, StandardCharsets.UTF_8);

                logData.setResponse(result);

                byte[] uppedContent = new String(content, StandardCharsets.UTF_8).getBytes();
                return exchange.getResponse().bufferFactory().wrap(uppedContent);
            }));
        }
    }
}