package com.tavares.recommender.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicRecommendationRequest {
    private String emotion;
    private String mediaType;
}
