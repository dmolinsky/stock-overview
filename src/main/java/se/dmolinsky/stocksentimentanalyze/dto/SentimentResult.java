package se.dmolinsky.stocksentimentanalyze.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResult {
    private String label;
    private Double score;
}