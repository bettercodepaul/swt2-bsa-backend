package de.bogenliga.application.services.v1.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    @MessageMapping("/send-message")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        return message;
    }
}