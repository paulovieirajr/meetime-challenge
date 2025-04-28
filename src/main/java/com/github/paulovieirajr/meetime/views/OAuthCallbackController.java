package com.github.paulovieirajr.meetime.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotPathController.OAUTH_SUCCESS_PATH;

@Controller
public class OAuthCallbackController {

    @GetMapping(OAUTH_SUCCESS_PATH)
    public String oauthSuccess(@RequestParam String session_id, Model model) {
        model.addAttribute("sessionId", session_id);
        return "success";
    }
}

