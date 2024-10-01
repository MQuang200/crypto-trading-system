package com.example.demo.util;

import com.example.demo.model.price.BinancePrice;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BinancePriceDeserializer implements JsonDeserializer<BinancePrice> {
    @Override
    public BinancePrice deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        BinancePrice binancePrice = new BinancePrice();
        binancePrice.setSymbol(jsonObject.get("symbol").getAsString());

        try {
            binancePrice.setBidPrice(new BigDecimal(jsonObject.get("bidPrice").getAsString()));
            binancePrice.setBidQty(new BigDecimal(jsonObject.get("bidQty").getAsString()));
            binancePrice.setAskPrice(new BigDecimal(jsonObject.get("askPrice").getAsString()));
            binancePrice.setAskQty(new BigDecimal(jsonObject.get("askQty").getAsString()));
        } catch (NumberFormatException e) {
            throw new JsonParseException("Error parsing numeric fields", e);
        }

        return binancePrice;
    }
}
