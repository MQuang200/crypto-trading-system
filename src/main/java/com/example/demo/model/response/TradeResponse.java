package com.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TradeResponse {
    private String status;
    private String message;
    private int transactionId;
    private String currencyPair;
    private String type;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal total;
    private Timestamp timestamp;

    public TradeResponse(String message) {
        this.status = "failed";
        this.message = message;
    }
}
