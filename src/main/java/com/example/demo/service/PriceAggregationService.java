package com.example.demo.service;

import com.example.demo.model.price.BestPrice;
import com.example.demo.model.price.BinancePrice;
import com.example.demo.model.price.HuobiPrice;
import com.example.demo.repository.PriceDAO;
import com.example.demo.util.BinancePriceDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PriceAggregationService {
    private final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private final String HUOBI_URL = "https://api.huobi.pro/market/tickers";
    private final Set<String> currencyPairs = new HashSet<>();

    @Autowired
    PriceDAO priceDAO;

    @Scheduled(fixedRate = 10000)
    public void aggregate() throws IOException, InterruptedException {
        currencyPairs.add("BTCUSDT");
        currencyPairs.add("ETHUSDT");

        // Create a new HttpClient
        HttpClient client = HttpClient.newHttpClient();
        System.out.println("Aggregating prices...");

        HttpResponse<String> binanceResponse = makeRequest(client, BINANCE_URL);
        HttpResponse<String> huobiResponse = makeRequest(client, HUOBI_URL);

        // Deserialize the JSON response from Binance and Huobi

        // Binance
        Gson binanceJsonHandler = new GsonBuilder()
                .registerTypeAdapter(BinancePrice.class, new BinancePriceDeserializer())
                .create();
        Type binancePriceType = new TypeToken<List<BinancePrice>>() {}.getType();
        List<BinancePrice> binanceRes = binanceJsonHandler.fromJson(binanceResponse.body(), binancePriceType);
        List<BinancePrice> filteredBinanceRes = binanceRes.stream().filter(elem ->
                currencyPairs.contains(elem.getSymbol().toUpperCase())).toList();

        // Huobi
        Gson huobiJsonHandler = new Gson();
        Type huobiPriceType = new TypeToken<List<HuobiPrice>>() {}.getType();
        JsonObject jsonHuobiPrice = huobiJsonHandler.fromJson(huobiResponse.body(), JsonObject.class);
        List<HuobiPrice> huobiRes = huobiJsonHandler.fromJson(jsonHuobiPrice.getAsJsonArray("data"), huobiPriceType);
        List<HuobiPrice> filteredHuobiRes = huobiRes.stream().filter(elem ->
                currencyPairs.contains(elem.getSymbol().toUpperCase())).toList();

        // Get the best bid and ask prices for each currency pair
        List<BestPrice> bestPrices = new ArrayList<>();

        for (String pair : currencyPairs) {
            BestPrice bestPrice = new BestPrice(pair, getBestBidPrice(filteredBinanceRes, filteredHuobiRes, pair),
                    getBestAskPrice(filteredBinanceRes, filteredHuobiRes, pair));
            bestPrices.add(bestPrice);
        }

        // Save the best prices to the database
        priceDAO.savePrice(bestPrices);
    }

    private BigDecimal getBestAskPrice(List<BinancePrice> filteredBinanceRes, List<HuobiPrice> filteredHuobiRes, String pair) {
        BinancePrice binancePrice = filteredBinanceRes.stream().filter(elem ->
                elem.getSymbol().equalsIgnoreCase(pair)).findAny().orElseThrow();

        HuobiPrice huobiPrice = filteredHuobiRes.stream().filter(elem ->
                elem.getSymbol().equalsIgnoreCase(pair)).findAny().orElseThrow();

        return binancePrice.getAskPrice().compareTo(huobiPrice.getAsk()) < 0 ? binancePrice.getAskPrice() : huobiPrice.getAsk();
    }

    private BigDecimal getBestBidPrice(List<BinancePrice> filteredBinanceRes, List<HuobiPrice> filteredHuobiRes, String pair) {
        BinancePrice binancePrice = filteredBinanceRes.stream().filter(elem ->
                elem.getSymbol().equalsIgnoreCase(pair)).findAny().orElseThrow();

        HuobiPrice huobiPrice = filteredHuobiRes.stream().filter(elem ->
                elem.getSymbol().equalsIgnoreCase(pair)).findAny().orElseThrow();

        return binancePrice.getBidPrice().compareTo(huobiPrice.getBid()) > 0 ? binancePrice.getBidPrice() : huobiPrice.getBid();
    }

    private HttpResponse<String> makeRequest(HttpClient client, String URL) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
