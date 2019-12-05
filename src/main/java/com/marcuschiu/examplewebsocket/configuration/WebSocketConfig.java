package com.marcuschiu.examplewebsocket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

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
//        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
//        registry.addEndpoint("/gs-guide-websocket")
//                .withSockJS();
//
//        // There's a reason why it has been separated (see link above)
//        registry.addEndpoint("/gs-guide-websocket")
//                .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
//                .setAllowedOrigins("*");

        // OPTION 2
//        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();

        // OPTION 3 - Intercept User Session ID
        // used in BasicController to Extract Session ID
        registry.addEndpoint("/gs-guide-websocket")
                .withSockJS();

        // There's a reason why it has been separated (see link above)
        registry.addEndpoint("/gs-guide-websocket")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        System.out.println("determineUser");
                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                            HttpSession session = servletRequest.getServletRequest().getSession();
                            attributes.put("sessionId", session.getId());
                            System.out.println("session id: " + session.getId());
                        }
                        return request.getPrincipal();
                    }
                })
                .setAllowedOrigins("*");
    }

}