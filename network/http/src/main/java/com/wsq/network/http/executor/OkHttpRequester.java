package com.wsq.network.http.executor;

import com.google.common.collect.Lists;
import com.wsq.network.http.interceptor.DefaultRetryInterceptor;
import com.wsq.util.common.config.ConfigUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author wsq
 * 2021/1/2 17:10
 */
@Slf4j
@Data
public class OkHttpRequester {
    protected volatile OkHttpClient httpClient;
    protected boolean enableLog;
    protected long timeoutMillis;
    protected Class retryStrategy;

    protected int default_retry_strategy_retries;
    protected long default_retry_strategy_retryIntervalMillis;

    public <T> OkHttpRequester(boolean enableLog, long timeoutMillis, Class<T> retryStrategy) {
        this.enableLog = enableLog;
        this.timeoutMillis = timeoutMillis;
        this.retryStrategy = retryStrategy;
        httpClient = newClient(enableLog, timeoutMillis, retryStrategy);
    }

    //请求被取消的响应标志
    private static final String CANCELED = "Canceled";

    //异步请求的异步请求的最大并发请求数，默认为64
    private static final Integer ASYNC_MAX_REQUESTS = ConfigUtils.getIntDefault("http.async.maxRequests", 64);

    //异步请求的异步请求的单个域名最大并发请求数，默认为5
    private static final Integer ASYNC_MAX_REQUESTS_PER_HOST = ConfigUtils.getIntDefault("http.async.maxRequestsPerHost", 5);

    //每个地址的最大连接数，默认为5
    private static final Integer MAX_IDLE_CONNECTIONS = ConfigUtils.getIntDefault("http.maxIdleConnections", 5);

    //连接的存活时间，单位为分钟，默认5分钟
    private static final Integer KEEP_ALIVE_DURATION = ConfigUtils.getIntDefault("http.keepAliveDuration", 5);

    private <T> OkHttpClient newClient(boolean enableLog, long timeoutMillis, Class<T> interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        try {
            //配置超时
            builder.connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
            builder.readTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
            builder.writeTimeout(timeoutMillis, TimeUnit.MILLISECONDS);

            //配置连接池
            builder.connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES));

            //配置网络协议
            //启用TLSv1和TLSv1.1，OkHttp3.13.0之后不再支持，此处特意添加 ConnectionSpec.COMPATIBLE_TLS
            //ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT为默认支持的配置
            builder.connectionSpecs(Lists.newArrayList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS));

            //配置异步请求的并发数
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(ASYNC_MAX_REQUESTS);
            dispatcher.setMaxRequestsPerHost(ASYNC_MAX_REQUESTS_PER_HOST);
            builder.dispatcher(dispatcher);

            //配置重试
            if (interceptor != null) {
                Interceptor retry;
                if (interceptor == DefaultRetryInterceptor.class) {
                    retry = new DefaultRetryInterceptor(default_retry_strategy_retries, default_retry_strategy_retryIntervalMillis);
                } else {
                    retry = (Interceptor) interceptor.newInstance();
                }

                builder.addInterceptor(retry);
            }

            //配置忽略客户端SSL证书
            X509TrustManager trustManager = new X509ExtendedTrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

            HostnameVerifier doNotVerify = (hostname, session) -> true;
            builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager).hostnameVerifier(doNotVerify);
        } catch (Exception e) {
            if (enableLog) {
                log.error("build okhttp client error", e);
            }
        }

        return builder.build();
    }
}
