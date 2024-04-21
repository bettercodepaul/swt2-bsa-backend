package de.bogenliga.application.services.v1.websocket;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    String test = "hi";
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

            TextMessage message = new TextMessage(objectMapper.writeValueAsString(test));
            session.sendMessage(message);

            Thread.sleep(1000);

        sessions.add(session);
    }
}