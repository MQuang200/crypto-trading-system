package com.example.demo.repository;

import com.example.demo.model.wallet.WalletBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WalletDAO {
    @Autowired
    DataSource dataSource;

    public List<WalletBalance> getBalance(String userId) {
       List<WalletBalance> walletBalances = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "select u.id as user_id, u.username, c.id as currency_id, c.symbol, c.name, coalesce(a.balance, 0) as balance\n" +
                    "from USERS u\n" +
                    "cross join CURRENCIES c\n" +
                    "left join ASSETS a\n" +
                    "    on u.id = a.user_id and c.id = a.currency_id\n" +
                    "where u.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                WalletBalance walletBalance = new WalletBalance();
                walletBalance.setUserId(Integer.parseInt(resultSet.getString("user_id")));
                walletBalance.setCurrencyId(Integer.parseInt(resultSet.getString("currency_id")));
                walletBalance.setCurrencySymbol(resultSet.getString("symbol"));
                walletBalance.setCurrencyName(resultSet.getString("name"));
                walletBalance.setBalance(resultSet.getBigDecimal("balance"));
                walletBalances.add(walletBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get wallet balance");
        }

        return walletBalances;
    }

    public void updateBalance(String userId, int currencyId, BigDecimal value) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO ASSETS (user_id, currency_id, balance) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(userId));
            statement.setInt(2, currencyId);
            statement.setBigDecimal(3, value);
            statement.setBigDecimal(4, value);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update balance");
        }
    }
}
