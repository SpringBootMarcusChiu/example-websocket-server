package com.marcuschiu.examplewebsocket.endpoint;

import com.marcuschiu.examplewebsocket.model.Greeting;
import com.marcuschiu.examplewebsocket.model.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AdvancedController {

    @MessageMapping("/channel/{channelId}")
    @SendTo("/topic/channel/{channelId}")
    public Greeting simple(@DestinationVariable String channelId, Message message) {
        System.out.println("AdvancedController: Channel - " + channelId + " Message Received - " + message.getName());
        System.out.println("AdvancedController: Channel - " + channelId + " Greeting Broadcast");
        return new Greeting("AdvancedController: Channel ID - " + channelId + " - Message Content - " + message.getName());
    }
}
