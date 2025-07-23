package se.magus.microservices.core.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.magus.microservices.core.chat.channel.request.SubscribeChannelRequest;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.data.message.request.PublicMessageRequest;
import se.magus.microservices.core.chat.exception.ChannelDoesNotExist;
import se.magus.microservices.core.chat.services.channel.PublicChannelService;
import se.magus.microservices.core.chat.services.message.PublicMessageService;

@Slf4j
@Controller
@RequestMapping(path = "/api/channel/public")
public class PublicChannelController {
    private final PublicMessageService messageService;
    private final PublicChannelService channelService;

    public PublicChannelController(PublicMessageService messageService, PublicChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    /*
        curl -POST http://localhost:8081/api/channel/public/publicMessage \
        -H "Content-Type: application/json" \
        -d '{"channelId": "c1d3a0d2-8e56-4b6f-b65a-93fcd2d1d70b", "message": "Hello world"}'
     */
    @RequestMapping(path = "/publicMessage", method = RequestMethod.POST)
    public ResponseEntity<Object> publishMessage(@RequestBody PublicMessageRequest request) {
        try {
            PublicMessageDto message = messageService.createMessage(
                    "fromUserId", request.getChannelId(), request.getMessage()
            );
            log.info("=========PublicChannelController - Message published: {}", message);
            messageService.deliverMessage(message);
            return ResponseEntity.ok(message);
        }catch (ChannelDoesNotExist e){
            return null;
        }

    }

    @RequestMapping(path = "/subscribe", method = RequestMethod.GET)
    public Object subscribe(SubscribeChannelRequest request) throws ChannelDoesNotExist {
        return channelService.subscribe(request.getChannelId());
    }
}
