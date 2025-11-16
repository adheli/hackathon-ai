package com.tavares.recommender.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicRecommendationResponse {
    private List<MusicRecommendation> items;
}
