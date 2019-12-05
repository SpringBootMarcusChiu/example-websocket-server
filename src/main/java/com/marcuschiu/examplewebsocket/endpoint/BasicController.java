package com.marcuschiu.examplewebsocket.endpoint;

import com.marcuschiu.examplewebsocket.model.Greeting;
import com.marcuschiu.examplewebsocket.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class BasicController {

    /**
     * @SendTo("/topic/greetings")
     * - client must subscribe to /topic/greetings
     * - every client would then receive a greeting
     */
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greetingBroadcast(Message message) throws Exception {
        System.out.println("received message from client: " + message.getName());
        System.out.println("wait one second - start");
        Thread.sleep(1000); // simulated delay
        System.out.println("wait one second - finished");
        System.out.println("sending message to client");
        return new Greeting("BasicController: Message Content - " + HtmlUtils.htmlEscape(message.getName()));
    }

    /**
     * @SendToUser("/topic/greetings")
     * - client must subscribe to /user/topic/greetings
     * - note: @SendToUser prepends /user to /topic/greetings
     * - only the client that sent message to this server's /hello/specific-user
     *   would receive the greeting
     */
    @MessageMapping("/hello/specific-user")
    @SendToUser("/topic/greetings")
    public Greeting greetingSpecificUser(Message message) throws Exception {
        System.out.println("received message from client: " + message.getName());
        System.out.println("wait one second - start");
        Thread.sleep(1000); // simulated delay
        System.out.println("wait one second - finished");
        System.out.println("sending message to client");
        return new Greeting("BasicController: Message Content - " + HtmlUtils.htmlEscape(message.getName()));
    }
}
