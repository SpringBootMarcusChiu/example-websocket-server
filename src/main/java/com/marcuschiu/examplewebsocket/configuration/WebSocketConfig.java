package com.marcuschiu.examplewebsocket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * With @EnableWebSocketMessageBroker we enabled a broker-backed messaging over
 * WebSocket using STOMP, which stands for Streaming Text Oriented Messaging Protocol.
 * It's important to remark that this annotation needs to be used in
 * conjunction with the @Configuration
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // https://stackoverflow.com/questions/30413380/websocketstompclient-wont-connect-to-sockjs-endpoint

        // OPTION 1
        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
        registry.addEndpoint("/gs-guide-websocket")
                .withSockJS();

        // There's a reason why it has been separated (see link above)
        registry.addEndpoint("/gs-guide-websocket")
                .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
                .setAllowedOrigins("*");

        // OPTION 2
//        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();
    }

}