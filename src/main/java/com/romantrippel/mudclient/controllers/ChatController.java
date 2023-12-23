package com.romantrippel.mudclient.controllers;

import com.romantrippel.mudclient.dto.InputMessageDTO;
import com.romantrippel.mudclient.dto.OutputMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessageDTO send (InputMessageDTO message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        return new OutputMessageDTO(message.getFrom(), message.getText(), time);
    }
}
