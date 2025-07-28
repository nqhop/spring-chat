package se.magus.microservices.core.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import se.magus.microservices.core.chat.data.message.request.PublicMessageRequest;

@WebMvcTest(PublicChannelController.class)
public class PublicChannelController {

    @Autowired
    private MockMvc mockMvc;


    /*
        curl -POST http://localhost:8081/api/channel/public/publicMessage \
        -H "Content-Type: application/json" \
        -d '{"channelId": "c1d3a0d2-8e56-4b6f-b65a-93fcd2d1d70b", "message": "Hello world"}'
     */
    @Test
    void sendPublicMessage(){
        PublicMessageRequest publicMessageRequest = new PublicMessageRequest("c1d3a0d2-8e56-4b6f-b65a-93fcd2d1d70b", "hello");

    }
}
