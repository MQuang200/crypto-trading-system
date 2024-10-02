package com.example.demo.controller;

import com.example.demo.model.wallet.WalletBalance;
import com.example.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WalletController {
    @Autowired
    WalletService walletService;

    @GetMapping("/wallet/balance/{user_id}")
    public ResponseEntity<List<WalletBalance>> getBalance(@PathVariable  String user_id){
        return ResponseEntity.ok(walletService.getBalance(user_id));
    }
}
