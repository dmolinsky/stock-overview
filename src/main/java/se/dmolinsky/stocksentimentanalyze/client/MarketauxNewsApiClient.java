package se.dmolinsky.stocksentimentanalyze.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.dmolinsky.stocksentimentanalyze.dto.MarketauxNewsResponse;
import se.dmolinsky.stocksentimentanalyze.model.NewsArticle;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MarketauxNewsApiClient {

    @Value("${marketaux.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<NewsArticle> getNewsByTicker(String ticker) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.marketaux.com/v1/news/all")
                .queryParam("symbols", ticker)
                .queryParam("language", "en")
                .queryParam("limit", "10")
                .queryParam("api_token", apiKey)
                .toUriString();

        MarketauxNewsResponse response = restTemplate.getForObject(url, MarketauxNewsResponse.class);

        return response.getData().stream()
                .map(article -> new NewsArticle(
                        ticker,
                        article.getTitle(),
                        article.getDescription(),
                        article.getUrl(),
                        article.getSource(),
                        article.getPublished_at(),
                        article.getOverallSentimentLabel(),
                        article.getOverallSentimentScore()
                ))
                .collect(Collectors.toList());
    }
}