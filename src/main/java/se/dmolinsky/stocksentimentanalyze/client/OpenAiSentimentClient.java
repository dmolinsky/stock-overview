package se.dmolinsky.stocksentimentanalyze.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiSentimentClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;
    
    public OpenAiSentimentClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String analyzeSentiment(String title, String summary) {
        String systemPrompt = "You are a sentiment analysis engine. " +
                "Given a title and summary of a news article, respond with: Positive, Neutral or Negative.";

        String userPrompt = "Title: " + title + "\nSummary: " + summary;

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", new Object[]{
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                },
                "temperature", 0
        );

        try {
            Map response = webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // vi kör den synkront för enkelhetens skull

            var choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return message.get("content").toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
