package com.example.demo.service;

import com.example.demo.model.wallet.WalletBalance;
import com.example.demo.repository.WalletDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {
    @Autowired
    WalletDAO walletDAO;

    public List<WalletBalance> getBalance(String userId) {
        return walletDAO.getBalance(userId);
    }
}
