package se.magus.microservices.core.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.magus.microservices.core.chat.channel.request.SubscribeChannelRequest;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.data.message.request.PublicMessageRequest;
import se.magus.microservices.core.chat.services.channel.PublicChannelService;
import se.magus.microservices.core.chat.services.message.PublicMessageService;

@Controller
@RequestMapping(path = "/api/channel/public")
public class PublicChannelController {
    private final PublicMessageService messageService;
    private final PublicChannelService channelService;

    public PublicChannelController(PublicMessageService messageService, PublicChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    @RequestMapping(path = "/publicMessage", method = RequestMethod.POST)
    public ResponseEntity<Object> publishMessage(@RequestBody PublicMessageRequest request) {
        PublicMessageDto message = messageService.createMessage(
                "fromUserId", "channelId", request.getMessage()
        );
        messageService.deliverMessage(message);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(path = "/subscribe", method = RequestMethod.GET)
    public Object subscribe(SubscribeChannelRequest request) {
        return channelService.subscribe(request.getChannelId());
    }
}
