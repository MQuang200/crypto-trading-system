package com.example.demo.service;

import com.example.demo.model.price.BestPrice;
import com.example.demo.model.request.TradeRequest;
import com.example.demo.model.response.TradeResponse;
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
    public String buy(String userId, TradeRequest tradeRequest) throws Exception{
        BestPrice currentPrice = priceService.getBestPrice(tradeRequest.getCurrencyPair());
        List<WalletBalance> walletBalances = walletService.getBalance(userId);

        WalletBalance usdtBalance = walletBalances.stream()
                .filter(walletBalance -> walletBalance.getCurrencySymbol().equals("USDT"))
                .findFirst()
                .orElse(null);
        BigDecimal total = currentPrice.getAskPrice().multiply(tradeRequest.getAmount());

        if (usdtBalance == null || usdtBalance.getBalance().compareTo(total) < 0) {
            return "Insufficient balance";
        }

        walletService.updateBalance(userId, usdtBalance.getCurrencyId(), usdtBalance.getBalance().subtract(total));

        String tradeCurrencySymbol = tradeRequest.getCurrencyPair().substring(0, tradeRequest.getCurrencyPair().length() - 4);
        WalletBalance tradeCurrencyBalance = walletBalances.stream()
                .filter(walletBalance -> walletBalance.getCurrencySymbol().equalsIgnoreCase(tradeCurrencySymbol))
                .findFirst()
                .orElse(null);

        assert tradeCurrencyBalance != null;
        walletService.updateBalance(userId, tradeCurrencyBalance.getCurrencyId()
                    , tradeRequest.getAmount().add(tradeCurrencyBalance.getBalance()));

        String result = tradeDAO.buy(userId, tradeRequest, "buy", currentPrice, total);

        return result;
    }
}
