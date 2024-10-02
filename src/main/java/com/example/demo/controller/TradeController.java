package com.example.demo.controller;

import com.example.demo.model.Transaction;
import com.example.demo.model.request.TradeRequest;
import com.example.demo.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TradeController {
    @Autowired
    TradeService tradeService;

    @PostMapping("/trade/buy/{user_id}")
    public ResponseEntity<String> buy(@PathVariable String user_id, @RequestBody TradeRequest tradeRequest) throws Exception{
        return ResponseEntity.ok(tradeService.buy(user_id, tradeRequest));

    }
    @PostMapping("/trade/sell/{user_id}")
    public ResponseEntity<String> sell(@PathVariable String user_id, @RequestBody TradeRequest tradeRequest) throws Exception{
        return ResponseEntity.ok(tradeService.sell(user_id, tradeRequest));
    }

    @GetMapping("/trade/history/{user_id}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String user_id) {
        return ResponseEntity.ok(tradeService.getTransactionHistory(user_id));
    }
}
