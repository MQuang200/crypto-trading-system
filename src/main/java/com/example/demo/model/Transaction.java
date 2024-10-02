package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    private int id;
    private int userId;
    private String currencyPair;
    private BigDecimal amount;
    private String type;
    private BigDecimal price;
    private BigDecimal total;
    private Timestamp createdAt;
}
