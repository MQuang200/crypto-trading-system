package com.example.demo.repository;

import com.example.demo.model.price.BestPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
                    statement.setString(1, price.getCurrentPair());
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
}
