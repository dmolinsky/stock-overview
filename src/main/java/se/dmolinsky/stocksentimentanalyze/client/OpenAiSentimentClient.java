package se.dmolinsky.stocksentimentanalyze.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import se.dmolinsky.stocksentimentanalyze.dto.SentimentResult;

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

    public SentimentResult analyzeSentiment(String title, String summary) {
        String systemPrompt = "You are a sentiment analysis engine. " +
                "Given the title and summary of a news article, respond only in JSON format with two fields: " +
                "'label' (Positive, Neutral, Negative) and 'score' (a decimal from -1.0 to 1.0 indicating sentiment strength).";

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
                    .block();

            var choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = message.get("content").toString().trim();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode contentNode = objectMapper.readTree(content);

            String label = contentNode.path("label").asText();
            double score = contentNode.path("score").asDouble();

            return new SentimentResult(label, score);

        } catch (WebClientResponseException.TooManyRequests e) {
            System.err.println("Rate limit exceeded. Waiting 10s before retry...");

            try {
                Thread.sleep(10_000);
                return analyzeSentiment(title, summary);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return new SentimentResult("Unknown", 0.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SentimentResult("Unknown", 0.0);
        }
    }
}
