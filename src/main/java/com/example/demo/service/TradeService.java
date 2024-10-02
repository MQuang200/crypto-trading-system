package com.example.demo.service;

import com.example.demo.model.Transaction;
import com.example.demo.model.price.BestPrice;
import com.example.demo.model.request.TradeRequest;
import com.example.demo.model.wallet.WalletBalance;
import com.example.demo.repository.PriceDAO;
import com.example.demo.repository.TradeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TradeService {
    @Autowired
    TradeDAO tradeDAO;

    @Autowired
    PriceService priceService;
    @Autowired
    private WalletService walletService;

    @Transactional(rollbackFor = Exception.class)
    public String buy(String userId, TradeRequest tradeRequest) {
        // Get the current price of the currency pair
        BestPrice currentPrice = priceService.getBestPrice(tradeRequest.getCurrencyPair());
        // Get the wallet balance of the user
        List<WalletBalance> walletBalances = walletService.getBalance(userId);

        // Check if the user has enough USDT balance to buy the currency
        WalletBalance usdtBalance = walletBalances.stream()
                .filter(walletBalance -> walletBalance.getCurrencySymbol().equals("USDT"))
                .findFirst()
                .orElse(null);
        BigDecimal total = currentPrice.getAskPrice().multiply(tradeRequest.getAmount());

        if (usdtBalance == null || usdtBalance.getBalance().compareTo(total) < 0) {
            return "Insufficient balance";
        }
        // Update the USDT balance of the user
        walletService.updateBalance(userId, usdtBalance.getCurrencyId(), usdtBalance.getBalance().subtract(total));

        // Update the balance of the currency being bought
        String tradeCurrencySymbol = tradeRequest.getCurrencyPair().substring(0, tradeRequest.getCurrencyPair().length() - 4);
        WalletBalance tradeCurrencyBalance = walletBalances.stream()
                .filter(walletBalance -> walletBalance.getCurrencySymbol().equalsIgnoreCase(tradeCurrencySymbol))
                .findFirst()
                .orElse(null);

        assert tradeCurrencyBalance != null;
        walletService.updateBalance(userId, tradeCurrencyBalance.getCurrencyId()
                    , tradeRequest.getAmount().add(tradeCurrencyBalance.getBalance()));

        // Insert the transaction into the database
        return tradeDAO.buy(userId, tradeRequest, "buy", currentPrice, total);
    }

    @Transactional
    public String sell(String userId, TradeRequest tradeRequest) {
        // Get the current price of the currency pair
        BestPrice currentPrice = priceService.getBestPrice(tradeRequest.getCurrencyPair());
        // Get the wallet balance of the user
        List<WalletBalance> walletBalances = walletService.getBalance(userId);

        // Check if the user has enough balance of the currency to sell
        String tradeCurrencySymbol = tradeRequest.getCurrencyPair().substring(0, tradeRequest.getCurrencyPair().length() - 4);
        WalletBalance tradeCurrencyBalance = walletBalances.stream()
                .filter(walletBalance -> walletBalance.getCurrencySymbol().equalsIgnoreCase(tradeCurrencySymbol))
                .findFirst()
                .orElse(null);

        if (tradeCurrencyBalance == null || tradeCurrencyBalance.getBalance().compareTo(tradeRequest.getAmount()) < 0) {
            return "Insufficient balance";
        }

        // Update the balance of the currency being sold
        walletService.updateBalance(userId, tradeCurrencyBalance.getCurrencyId()
                , tradeCurrencyBalance.getBalance().subtract(tradeRequest.getAmount()));

        // Update the USDT balance of the user
        WalletBalance usdtBalance = walletBalances.stream()
                .filter(walletBalance -> walletBalance.getCurrencySymbol().equals("USDT"))
                .findFirst()
                .orElse(null);
        BigDecimal total = currentPrice.getBidPrice().multiply(tradeRequest.getAmount());
        walletService.updateBalance(userId, usdtBalance.getCurrencyId(), usdtBalance.getBalance().add(total));

        // Insert the transaction into the database
        return tradeDAO.sell(userId, tradeRequest, "sell", currentPrice, total);
    }

    public List<Transaction> getTransactionHistory(String userId) {
        return tradeDAO.getTransactionHistory(userId);
    }
}
