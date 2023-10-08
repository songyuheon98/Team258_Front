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
    public ChatingContent greeting(ChatMessageDto message) throws Exception {
        Thread.sleep(100);
        return new ChatingContent( HtmlUtils.htmlEscape(message.getChatingMessage()) );
    }
}
