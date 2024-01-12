package com.chatty.service.match;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchWebController {

    @GetMapping("/rooms")
    public String rooms() {
        return "match";
    }

}
