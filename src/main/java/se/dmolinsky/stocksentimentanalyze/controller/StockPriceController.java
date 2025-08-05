package se.dmolinsky.stocksentimentanalyze.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.dmolinsky.stocksentimentanalyze.client.AlphaVantageClient;

@RestController
public class StockPriceController {

    private final AlphaVantageClient client;

    public StockPriceController(AlphaVantageClient client) {
        this.client = client;
    }

    @GetMapping("/ticker")
    public String getStockPrice(@RequestParam String ticker) {
        return client.fetchIntradayPrices(ticker);
    }
}