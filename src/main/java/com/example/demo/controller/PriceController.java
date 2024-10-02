package com.example.demo.controller;

import com.example.demo.model.price.BestPrice;
import com.example.demo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {
    @Autowired
    PriceService priceService;

    @GetMapping("/price/{pair}")
    public ResponseEntity<BestPrice> getBestPrice(@PathVariable String pair){
        return ResponseEntity.ok(priceService.getBestPrice(pair));
    }
}
