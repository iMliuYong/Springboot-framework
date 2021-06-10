package com.quickshare.framework.web.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RestTemplateConfiguration
 */
@Configuration
@ConditionalOnClass(value = {RestTemplate.class, HttpClient.class})
@ConfigurationProperties(prefix = "config.web.resttemplate")
public class RestTemplateConfiguration {

    /**
     * 连接池的最大连接数,默认为0
     */
    private int maxTotalConnect=0;

    /**
     * 单个主机的最大连接数
     */
    private int maxConnectPerRoute=0;

    /**
     * 连接超时,默认5s
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时,默认60s
     */
    private int readTimeout = 60000;

    public int getMaxTotalConnect() {
        return maxTotalConnect;
    }

    public void setMaxTotalConnect(int maxTotalConnect) {
        this.maxTotalConnect = maxTotalConnect;
    }

    public int getMaxConnectPerRoute() {
        return maxConnectPerRoute;
    }

    public void setMaxConnectPerRoute(int maxConnectPerRoute) {
        this.maxConnectPerRoute = maxConnectPerRoute;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /*private ClientHttpRequestFactory createFactory() {
        if (this.maxTotalConnect <= 0) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(this.connectTimeout);
            factory.setReadTimeout(this.readTimeout);
            return factory;
        }
        HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(this.maxTotalConnect)
                .setMaxConnPerRoute(this.maxConnectPerRoute).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                httpClient);
        factory.setConnectTimeout(this.connectTimeout);
        factory.setReadTimeout(this.readTimeout);

        return factory;
    }*/

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();//this.createFactory()
        return restTemplate;
    }
}