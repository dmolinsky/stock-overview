package se.dmolinsky.stocksentimentanalyze.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.dmolinsky.stocksentimentanalyze.client.AlphaVantageClient;
import se.dmolinsky.stocksentimentanalyze.model.PricePoint;

import java.util.List;

@Controller
public class GraphController {

    private final AlphaVantageClient client;

    public GraphController(AlphaVantageClient client) {
        this.client = client;
    }

    @GetMapping("/graph")
    public String getStockPrice(@RequestParam String ticker, Model model) {
        List<PricePoint> prices = client.fetchIntradayPrices(ticker);
        model.addAttribute("prices", prices);
        model.addAttribute("ticker", ticker);
        return "graph";
    }
}