package com.chatty.service.match;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final MatchHandler matchHandler;
    private final Gson gson;

    @GetMapping("/rooms")
    public String rooms() {
        return "match";
    }

//    @GetMapping("/match")
//    public String enter(@RequestParam("username") String username) {
//        System.out.println("도착");
//        return "match";
//    }
}
