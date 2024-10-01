package com.example.demo.model.price;

import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinancePrice {
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private BigDecimal askPrice;
    private BigDecimal askQty;

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}