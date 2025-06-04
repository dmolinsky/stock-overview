package se.dmolinsky.stocksentimentanalyze.service;

import org.springframework.stereotype.Service;
import se.dmolinsky.stocksentimentanalyze.client.MarketauxNewsApiClient;
import se.dmolinsky.stocksentimentanalyze.client.OpenAiSentimentClient;
import se.dmolinsky.stocksentimentanalyze.dto.SentimentResult;
import se.dmolinsky.stocksentimentanalyze.model.NewsArticle;
import se.dmolinsky.stocksentimentanalyze.model.SentimentAnalysisResult;

import java.util.List;

@Service
public class NewsService {

    private final MarketauxNewsApiClient newsClient;
    private final OpenAiSentimentClient sentimentClient;

    public NewsService(MarketauxNewsApiClient newsClient, OpenAiSentimentClient sentimentClient) {
        this.newsClient = newsClient;
        this.sentimentClient = sentimentClient;
    }

    public SentimentAnalysisResult getAnalyzedArticles(String ticker) {
        List<NewsArticle> articles = newsClient.getNewsByTicker(ticker);

        double totalScore = 0.0;
        int count = 0;


        // TEMP CODE
        for (NewsArticle article : articles) {
            SentimentResult sentiment = new SentimentResult("Neutral", 0.1);

            article.setSentimentLabel(sentiment.getLabel());
            article.setSentimentScore(sentiment.getScore());


            if (sentiment.getScore() != null) {
                totalScore += sentiment.getScore();
                count++;
            }

        /* REAL CODE
        for (NewsArticle article : articles) {
            SentimentResult sentiment = sentimentClient.analyzeSentiment(
                    article.getTitle(), article.getDescription());

            article.setSentimentLabel(sentiment.getLabel());
            article.setSentimentScore(sentiment.getScore());



            if (sentiment.getScore() != null) {
                totalScore += sentiment.getScore();
                count++;
            }

             */
        }

        double avg = count > 0 ? totalScore / count : 0.0;
        return new SentimentAnalysisResult(articles, avg);
    }
}