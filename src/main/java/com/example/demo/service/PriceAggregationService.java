package com.example.demo.services;

import com.google.gson.Gson;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class PriceAggregationService {
    private final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private final String HOUBI_URL = "https://api.huobi.pro/market/tickers";
    @Scheduled(fixedRate = 10000)
    public void aggregate() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest binanceRequest = HttpRequest.newBuilder()
                .uri(URI.create(BINANCE_URL))
                .GET()
                .build();

        HttpResponse<String> binanceResponse = client.send(binanceRequest, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        System.out.println("Aggregating prices...");
        System.out.println(binanceResponse.body());
    }
}
