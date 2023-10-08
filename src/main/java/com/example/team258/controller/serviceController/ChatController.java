package com.example.team258.controller.serviceController;

import com.example.team258.entity.ChatingContent;
import com.example.team258.entity.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {
    @MessageMapping("/chat")
    @SendTo("/chatMessage")
    public ChatingContent chat(ChatMessageDto message) throws Exception {
        return new ChatingContent( HtmlUtils.htmlEscape(message.getChatingMessage()) );
    }
}
