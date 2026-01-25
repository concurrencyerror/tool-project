package com.horace.toolbackend.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class MyRestClientConfig {

    @Bean(destroyMethod = "close")
    public CloseableHttpClient apacheHttpClient() {

        //超时配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(2))
                .build();


        //配置连接池管理器
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                //连接的相关配置
                .setDefaultConnectionConfig(connectionConfig)
                //最大连接数
                .setMaxConnTotal(200)
                //最大同一域名端口最大连接数
                .setMaxConnPerRoute(50)
                .build();


        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(2)) //这是等待连接池的时间，不是请求时间
                .setResponseTimeout(Timeout.ofSeconds(10)) //这是相应时间
                .build();


        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(Timeout.ofSeconds(30))
                .build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient apacheHttpClient) {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(apacheHttpClient);

        factory.setConnectionRequestTimeout(Duration.ofSeconds(2));
        factory.setReadTimeout(Duration.ofSeconds(10));
        return factory;
    }

    /**
     * 增加定制化的factory，避免干扰mock进行
     *
     * @param factory 定制化factory
     * @return RestClientCustomizer
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public RestClientCustomizer restClientCustomizer(ClientHttpRequestFactory factory) {
        return builder -> builder.requestFactory(factory);
    }

    /**
     * 用spring framework内部生成的builder来进行处理，否则测试的mock可能会被绕过
     * @param builder RestClient.Builder 从spring framework内部生成的builder
     * @return RestClient
     */
    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        // 用 Boot 预配置过的 builder（含 message converters），只替换底层请求工厂
        return builder.build();
    }
}
