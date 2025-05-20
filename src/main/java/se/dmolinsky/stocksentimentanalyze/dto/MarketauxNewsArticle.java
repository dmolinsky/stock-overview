package se.dmolinsky.stocksentimentanalyze.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarketauxNewsArticle {
    private String title;
    private String description;
    private String published_at;
    private String source;
    private String url;
    private List<String> symbols;
    private Double overallSentimentScore;
    private String overallSentimentLabel;
}