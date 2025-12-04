package com.chat.controller;

import com.chat.dto.ClientMessage;
import com.chat.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
public class WSChatsController{

    @Autowired
    private ProducerService producerService;

    @MessageMapping("/chat1")
    public void getMessage (ClientMessage message) {

        producerService.sendMessage(message);
    }
}
