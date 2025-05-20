package se.dmolinsky.stocksentimentanalyze.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MarketauxNewsResponse {
    private List<MarketauxNewsArticle> data;

    public List<MarketauxNewsArticle> getData() {
        return data;
    }
}