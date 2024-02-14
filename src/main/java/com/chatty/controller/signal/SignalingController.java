package com.chatty.controller.signal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SignalingController {

    @MessageMapping("/peer/offer/{roomId}")
    @SendTo("/topic/peer/offer/{roomId}")
    public String PeerHandleOffer(@Payload String offer, @DestinationVariable(value = "roomId") String roomId){
        log.info("[OFFER] {}", offer);
        return offer;
    }

    @MessageMapping("/peer/iceCandidate/{roomId}")
    @SendTo("/topic/peer/iceCandidate/{roomId}")
    public String PeerHandleIceCandidate(@Payload String candidate, @DestinationVariable(value ="roomId") String roomId){
        log.info("[ICECANDIDATE] {}", candidate);
        return candidate;
    }

    @MessageMapping("/peer/answer/{roomId}")
    @SendTo("/topic/peer/answer/{roomId}")
    public String PeerHandleAnswer(@Payload String answer, @DestinationVariable(value = "roomId") String roomId) {
        log.info("[ANSWER] {}", answer);
        return answer;
    }
}
