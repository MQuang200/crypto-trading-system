package com.example.demo.service;

import com.example.demo.model.wallet.WalletBalance;
import com.example.demo.repository.WalletDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {
    @Autowired
    WalletDAO walletDAO;

    public List<WalletBalance> getBalance(String userId) {
        return walletDAO.getBalance(userId);
    }

    public void updateBalance(String userId, int currencyId, BigDecimal value) {
       walletDAO.updateBalance(userId, currencyId, value);
    }
}
