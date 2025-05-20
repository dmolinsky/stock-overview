package se.dmolinsky.stocksentimentanalyze.controller;

import se.dmolinsky.stocksentimentanalyze.client.MarketauxNewsApiClient;
import se.dmolinsky.stocksentimentanalyze.model.NewsArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private MarketauxNewsApiClient newsApiClient;

    @GetMapping("/news")
    public String getNews(@RequestParam(defaultValue = "NVDA") String ticker, Model model) {
        List<NewsArticle> articles = newsApiClient.getNewsByTicker(ticker);
        model.addAttribute("articles", articles);
        model.addAttribute("ticker", ticker);
        return "news";
    }
}