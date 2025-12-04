package com.chat.service;

import com.chat.dto.ClientMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToChannel(ClientMessage message){

        //el destino es el prefijo del broker el cual configure en las config del socket como "/server" y el canal el cual es el canal de la mancha
        String destination = "/server/canal-de-la-mancha";

        //esto es para logear si el mensaje se está enviando.
        log.info("enviando mensaje de difusion a STOMP destino: {}", destination);

        try {
            messagingTemplate.convertAndSend(destination, message);
        }catch (Exception ex){
            log.error("Error al enviar el mensaje por websocket a {}: {}", destination , ex.getMessage());
        }

    }

}
