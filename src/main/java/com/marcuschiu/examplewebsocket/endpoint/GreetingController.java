package com.marcuschiu.examplewebsocket.endpoint;

import com.marcuschiu.examplewebsocket.model.Greeting;
import com.marcuschiu.examplewebsocket.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        System.out.println("received message from client: " + message.getName());
        System.out.println("wait one second - start");
        Thread.sleep(1000); // simulated delay
        System.out.println("wait one second - finished");
        System.out.println("sending message to client");
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
