package com.tavares.recommender.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MusicController {

    private final MusicService recommendationService;

    @Autowired
    public MusicController(MusicService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/recommend")
    public String recommend(@RequestParam String prompt, @RequestParam int count, Model model) {
        String recommendations = recommendationService.getRecommendations(prompt, count);
        model.addAttribute("prompt", prompt);
        model.addAttribute("count", count);
        model.addAttribute("recommendations", recommendations);
        return "index";
    }
}

