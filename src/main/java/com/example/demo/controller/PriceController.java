package com.example.demo.controller;

import com.example.demo.model.price.BestPrice;
import com.example.demo.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Price", description = "Price API")
public class PriceController {
    @Autowired
    PriceService priceService;

    @Operation(summary = "Get best price for a currency pair")
    @GetMapping("/price/{pair}")
    public ResponseEntity<BestPrice> getBestPrice(@PathVariable String pair){
        return ResponseEntity.ok(priceService.getBestPrice(pair));
    }
}
