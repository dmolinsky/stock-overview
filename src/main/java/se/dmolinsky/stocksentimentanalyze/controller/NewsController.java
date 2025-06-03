package se.dmolinsky.stocksentimentanalyze.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.dmolinsky.stocksentimentanalyze.model.SentimentAnalysisResult;
import se.dmolinsky.stocksentimentanalyze.service.NewsService;


@Controller
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public String showNews(@RequestParam String ticker, Model model) {
        SentimentAnalysisResult result = newsService.getAnalyzedArticles(ticker);

        model.addAttribute("articles", result.getArticles());
        model.addAttribute("averageScore", result.getAverageScore());
        model.addAttribute("ticker", ticker);
        return "news";
    }
}