package se.dmolinsky.stocksentimentanalyze.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PricePoint {
    private String time;
    private double price;

    public PricePoint(String time, double price) {
        this.time = time;
        this.price = price;
    }
}
