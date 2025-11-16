
package com.tavares.recommender.controller;

import com.tavares.recommender.service.MusicType;
import com.tavares.recommender.service.MusicService;
import com.tavares.recommender.service.MoodAnalysisService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/flow")
public class FlowController {

    private final MoodAnalysisService moodAnalysisService;
    private final MusicService musicService;

    public FlowController(MoodAnalysisService moodAnalysisService, MusicService musicService) {
        this.moodAnalysisService = moodAnalysisService;
        this.musicService = musicService;
    }

    @PostMapping("/analyze")
    public String analyzeMood(@RequestParam String moodDescription, Model model) {
        List<String> emotions = moodAnalysisService.extractEmotions(moodDescription);
        model.addAttribute("emotions", emotions);
        return "emotion-select";
    }

    @PostMapping("/choose")
    public String chooseType(@RequestParam String emotion, Model model) {
        model.addAttribute("emotion", emotion);
        return "recommendation-choice";
    }

    @PostMapping("/recommend")
    public String recommend(
            @RequestParam String emotion,
            @RequestParam String type,
            Model model) {

        MusicType mediaType = MusicType.valueOf(type.toUpperCase());
        String results = musicService.recommend(emotion, mediaType);

        model.addAttribute("emotion", emotion);
        model.addAttribute("results", results);
        return "results";
    }
}
