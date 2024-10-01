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
public class HuobiPrice {
  private String symbol;
  private BigDecimal open;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal close;
  private BigDecimal amount;
  private BigDecimal vol;
  private int count;
  private BigDecimal bid;
  private BigDecimal bidSize;
  private BigDecimal ask;
  private BigDecimal askSize;

  @Override
     public String toString() {
        return "HuobiPrice{" +
                "symbol='" + symbol + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", amount=" + amount +
                ", vol=" + vol +
                ", count=" + count +
                ", bid=" + bid +
                ", bidSize=" + bidSize +
                ", ask=" + ask +
                ", askSize=" + askSize +
                '}';
    }
}