package com.chat.config.webSocketConfig;

import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //Habilita el soporte de websocket en Spring
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //Este metodo define el agente de mensajes y como se enrutan los mensajes
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/server");//esta ruta es para distribuir el mensaje de entrada entre todos los subscritos a esta url
        registry.setApplicationDestinationPrefixes("/app");//esta es la ruta para conectar con el controller
    }

    //Este metodo define el endpoint inicial que el cliente utiliza para establecer la conexion websocket
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();//esta es la ruta que establece que se iniciara una conexion websocket desde el cliente
    }
}
