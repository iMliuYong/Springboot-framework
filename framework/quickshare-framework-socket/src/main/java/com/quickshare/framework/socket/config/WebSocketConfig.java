package com.quickshare.framework.socket.config;

import com.quickshare.framework.socket.server.ServerEndpointExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu_ke
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        ServerEndpointExporter endpointExporter = new ServerEndpointExporter();
        return endpointExporter;
    }
}
