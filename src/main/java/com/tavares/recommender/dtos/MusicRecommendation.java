package com.tavares.recommender.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicRecommendation {
    private String title;
    private String artist;
    private String description;
}
