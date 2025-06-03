package se.dmolinsky.stocksentimentanalyze.model;

import java.util.List;

public class SentimentAnalysisResult {
    private List<NewsArticle> articles;
    private double averageScore;

    public SentimentAnalysisResult(List<NewsArticle> articles, double averageScore) {
        this.articles = articles;
        this.averageScore = averageScore;
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }

    public double getAverageScore() {
        return averageScore;
    }
}
