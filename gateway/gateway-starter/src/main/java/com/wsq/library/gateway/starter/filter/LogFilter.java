package com.wsq.library.gateway.starter.filter;

import com.wsq.library.gateway.starter.util.IpUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;

/**
 * @author wsq
 * @Description
 * @date 2021/12/1 19:41
 */
public class LogFilter implements WebFilter {
    public static final String POST_BODY_PARAM = "postBodyParam";
    private static final String START_TIME_MIL = "startTime";
    private static final String CLIENT_IP = "clientIp";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        StringBuilder sb = new StringBuilder();

        exchange.getAttributes().put(CLIENT_IP, IpUtils.getIpAddress(request));
        exchange.getAttributes().put(START_TIME_MIL, Clock.systemUTC().millis());

        return DataBufferUtils.join(request.getBody()).flatMap(dataBuffer -> {
            DataBufferUtils.retain(dataBuffer);
            exchange.getAttributes().put(POST_BODY_PARAM, dataBuffer);

            return chain.filter(exchange.mutate().
                    request(new PartnerServerHttpRequestDecorator(request, dataBuffer)).
                    response(new PartnerServerHttpResponseDecorator(request, dataBuffer)).
                    build());
        });
    }

    public static class PartnerServerHttpRequestDecorator extends ServerHttpRequestDecorator {
        private final DataBuffer dataBuffer;

        public PartnerServerHttpRequestDecorator(ServerHttpRequest delegate, DataBuffer dataBuffer) {
            super(delegate);
            this.dataBuffer = dataBuffer;
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
        }
    }

    public static class PartnerServerHttpResponseDecorator extends ServerHttpResponseDecorator {
        public PartnerServerHttpResponseDecorator(ServerHttpResponse delegate) {
            super(delegate);
        }

        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            return super.writeWith(body);
        }
    }
}