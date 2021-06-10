package com.quickshare;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootConfig {

    @Bean
    public ServletWebServerFactory servletContainer(){
        UndertowServletWebServerFactory undertow = new UndertowServletWebServerFactory();
        return undertow;
    }
}