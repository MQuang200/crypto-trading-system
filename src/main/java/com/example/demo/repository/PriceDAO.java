package com.example.demo.repository;

import com.example.demo.model.price.BestPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class PriceDAO {
    @Autowired
    DataSource datasource;

    public void savePrice(List<BestPrice> prices){
        try(Connection connection = datasource.getConnection()){
            String sql = "INSERT INTO PRICES (currency_pair, bid_price, ask_price, created_at) VALUES (?, ?, ?, ?)";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                for (BestPrice price : prices){
                    statement.setString(1, price.getCurrencyPair());
                    statement.setBigDecimal(2, price.getBidPrice());
                    statement.setBigDecimal(3, price.getAskPrice());
                    statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public BestPrice getBestPrice(String currencyPair){
        BestPrice result = null;
        try (Connection connection = datasource.getConnection()){
            String sql = "SELECT * FROM PRICES WHERE currency_pair = ? ORDER BY created_at DESC LIMIT 1";
            try (CallableStatement statement = connection.prepareCall(sql)){
                statement.setString(1, currencyPair.toUpperCase());
                ResultSet rs = statement.executeQuery();

                while (rs.next()){
                    result = new BestPrice();
                    result.setCurrencyPair(rs.getString("currency_pair"));
                    result.setBidPrice(rs.getBigDecimal("bid_price"));
                    result.setAskPrice(rs.getBigDecimal("ask_price"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
