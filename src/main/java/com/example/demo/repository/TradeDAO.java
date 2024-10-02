package com.example.demo.repository;

import com.example.demo.model.price.BestPrice;
import com.example.demo.model.request.TradeRequest;
import com.example.demo.model.response.TradeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class TradeDAO {
    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String buy(String userId, TradeRequest tradeRequest, String type,
                             BestPrice currentPrice, BigDecimal total) {
        String sql = "INSERT INTO TRANSACTIONS (user_id, currency_pair, amount, type, price, total, created_at)\n "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, Integer.parseInt(userId), tradeRequest.getCurrencyPair(), tradeRequest.getAmount(),
                type, currentPrice.getAskPrice(), total, new Timestamp(System.currentTimeMillis()));
        return "Success buy";
    }

    public String sell(String userId, TradeRequest tradeRequest, String type,
                             BestPrice currentPrice, BigDecimal total) {
        String sql = "INSERT INTO TRANSACTIONS (user_id, currency_pair, amount, type, price, total, created_at)\n "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, Integer.parseInt(userId), tradeRequest.getCurrencyPair(), tradeRequest.getAmount(),
                type, currentPrice.getBidPrice(), total, new Timestamp(System.currentTimeMillis()));
        return "Success sell";
    }
}
