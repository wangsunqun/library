package com.wsq.library.gateway.starter.filter;

import com.wsq.library.gateway.starter.common.exception.GatewayException;
import com.wsq.library.gateway.starter.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shenyu.common.utils.GsonUtils;
import org.apache.shenyu.plugin.api.result.ShenyuResultWrap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class GlobalExceptionFilter extends DefaultErrorWebExceptionHandler {

    public GlobalExceptionFilter(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ServerProperties serverProperties,
                                 ObjectProvider<List<ViewResolver>> viewResolverProvider, ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, serverProperties.getError(), applicationContext);
        setViewResolvers(viewResolverProvider.getIfAvailable(Collections::emptyList));
        setMessageWriters(serverCodecConfigurer.getWriters());
        setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        LogUtils logData = exchange.getAttribute(LogFilter.LOG_DATA);

        Mono<Void> handle = super.handle(exchange, throwable);
        return Objects.isNull(logData) ? handle : handle.then(Mono.fromRunnable(logData::log));
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        log.info("全局异常拦截!", getError(request));

        return response(request);
    }

    private String formatError(Throwable throwable, ServerRequest request) {
        String reason = throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
        return "Resolved [" + reason + "] for HTTP " + request.methodName() + " " + request.path();
    }

    private Map<String, Object> response(ServerRequest request) {
        Throwable error = getError(request);

        Object ex;
        if (error instanceof GatewayException) {
            GatewayException gatewayException = (GatewayException) error;
            ex = ShenyuResultWrap.error(gatewayException.getStatus(), gatewayException.getMessage(), error.getMessage());
        } else {
            ex = ShenyuResultWrap.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), error.getMessage());
        }

        return GsonUtils.getInstance().toObjectMap(GsonUtils.getInstance().toJson(ex));
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}