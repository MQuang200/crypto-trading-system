package com.example.demo.repository;

import com.example.demo.model.wallet.WalletBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
            String sql = "select u.id, u.username, c.id, c.symbol, c.name, coalesce(a.balance, 0) as balance\n" +
                    "from USERS u\n" +
                    "cross join CURRENCIES c\n" +
                    "left join ASSETS a\n" +
                    "    on u.id = a.users_id and c.id = a.currency_id\n" +
                    "where u.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                WalletBalance walletBalance = new WalletBalance();
                walletBalance.setCurrencySymbol(resultSet.getString("symbol"));
                walletBalance.setCurrencyName(resultSet.getString("name"));
                walletBalance.setBalance(resultSet.getBigDecimal("balance"));
                walletBalances.add(walletBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return walletBalances;
    }
}
