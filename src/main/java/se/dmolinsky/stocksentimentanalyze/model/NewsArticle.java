package se.dmolinsky.stocksentimentanalyze.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    private String ticker;

    private String title;
    private String description;
    private String url;
    private String source;
    private String publishedAt; // ISO8601-format (ex: "2025-05-15T12:00:00Z")

    private String sentimentLabel;   // Positive, Neutral, Negative
    private Double sentimentScore;
}