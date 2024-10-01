package com.example.demo.model.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BestPrice {
    private String currentPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;

    @Override
    public String toString() {
        return "BestPrice{" +
                "currentPair='" + currentPair + '\'' +
                ", bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                '}';
    }
}
