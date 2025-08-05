package se.dmolinsky.stocksentimentanalyze.client;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.dmolinsky.stocksentimentanalyze.model.PricePoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Component
public class AlphaVantageClient {

    @Value("${ALPHA_VANTAGE_API_KEY}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchIntradayPrices(String symbol) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.alphavantage.co/query")
                .queryParam("function", "TIME_SERIES_INTRADAY")
                .queryParam("symbol", symbol)
                .queryParam("interval", "5min")
                .queryParam("outputsize", "compact") // 100 outputs = one day
                .queryParam("apikey", apiKey)
                .toUriString();


        String json = restTemplate.getForObject(url, String.class);
        return parseIntradayPrices(json).toString();
    }

    private List<PricePoint> parseIntradayPrices(String json) {
        List<PricePoint> result = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode timeSeries = root.get("Time Series (5min)");

            if (timeSeries == null) return result;

            Iterator<String> times = timeSeries.fieldNames();
            while (times.hasNext()) {
                String time = times.next();
                double closePrice = timeSeries.get(time).get("4. close").asDouble();
                result.add(new PricePoint(time, closePrice));
            }

            result.sort(Comparator.comparing(PricePoint::getTime));
        } catch (Exception e) {
            e.printStackTrace(); // logga bättre i riktig applikation
        }

        return result;
    }
}