package ro.tuc.ds2020.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send_msg")
    public void sendMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSend("/topic/msg_from_" + message.getFrom(), message);
    }

    @MessageMapping("/send_read")
    public void sendReadNotification(@Payload ReadOrTypingNotification notification) {
        simpMessagingTemplate.convertAndSend("/topic/read_from_" + notification.getFrom(), notification);
    }

    @MessageMapping("/send_typing")
    public void sendTypingNotification(@Payload ReadOrTypingNotification notification) {
        simpMessagingTemplate.convertAndSend("/topic/typing_from_" + notification.getFrom(), notification);
    }
}
