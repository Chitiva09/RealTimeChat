package com.chat.config.kafka.listeners;

import com.chat.dto.ClientMessage;
import com.chat.service.WebSocketService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ConsumerListener {

    @Autowired
    private WebSocketService webSocketService;

    @KafkaListener(groupId = "group1", topics = "canal-de-la-mancha",
    containerFactory = "validMessageContainerFactory")
    public void listener1 (ClientMessage clientMessage){
        log.info("Mensaje del chat recibido. Remitente: {}, Contenido: {}", clientMessage.user(), clientMessage.text());

        webSocketService.sendToChannel(clientMessage);

    }

}
