package com.example.demo.service;

import com.example.demo.model.price.BestPrice;
import com.example.demo.repository.PriceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    @Autowired
    PriceDAO priceDAO;

    public BestPrice getBestPrice(String pair) {
        return priceDAO.getBestPrice(pair);
    }
}
