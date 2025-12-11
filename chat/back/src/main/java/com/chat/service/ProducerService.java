package com.chat.service;

import com.chat.dto.ClientMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, ClientMessage> kafkaTemplate;

    public void sendMessage(ClientMessage message){

        String key = message.user();

        kafkaTemplate.send("canal-de-la-mancha", key,message).whenComplete((result, exception) -> {
            if (exception != null){
                log.error("Error al enviar el mensaje: {}", exception.getMessage());
            }
            log.info("Mensaje enviado con éxito: {}", result.getProducerRecord().value());
        });

    }
}
