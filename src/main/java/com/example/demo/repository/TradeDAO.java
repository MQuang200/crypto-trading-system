package com.example.demo.repository;

import com.example.demo.model.price.BestPrice;
import com.example.demo.model.request.TradeRequest;
import com.example.demo.model.response.TradeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class TradeDAO {
    @Autowired
    DataSource dataSource;

    public String buy(String userId, TradeRequest tradeRequest, String type,
                             BestPrice currentPrice, BigDecimal total) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO TRANSACTIONS (user_id, currency_pair, amount, type, price, total) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(userId));
            statement.setString(2, tradeRequest.getCurrencyPair());
            statement.setBigDecimal(3, tradeRequest.getAmount());
            statement.setString(4, type);
            statement.setBigDecimal(5, currentPrice.getAskPrice());
            statement.setBigDecimal(6, total);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to buy");
        }
        return "Success buy";
    }
}
