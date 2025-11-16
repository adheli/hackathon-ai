
package com.tavares.recommender.prompts;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;

@Data
@AllArgsConstructor
public class PromptLoader {

    private String filePrompt;
    private String defaultSystemPrompt;

    public String loadPromptAsString() {
        try {
            return new String(
                new ClassPathResource("prompts/" + this.filePrompt).getInputStream().readAllBytes()
            );
        } catch (IOException e) {
            return this.defaultSystemPrompt;
        }
    }
}
