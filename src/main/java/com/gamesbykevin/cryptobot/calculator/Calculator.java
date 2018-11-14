package com.gamesbykevin.cryptobot.calculator;

import com.gamesbykevin.cryptobot.history.History;
import com.gamesbykevin.cryptobot.util.GsonHelper;
import com.gamesbykevin.cryptobot.util.JsonHelper;
import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class Calculator {

    //our historical data
    private History history;

    //the current price of what we are trading
    private BigDecimal price;

    //where do we get our market data?
    private final String dataFeedUrl;

    //where do we get the current price of the stock we are trading
    private final String tickerPriceUrl;

    /**
     * Default constructor
     */
    public Calculator(final String dataFeedUrl, final String tickerPriceUrl) {
        this.dataFeedUrl = dataFeedUrl;
        this.tickerPriceUrl = tickerPriceUrl;
        this.history = new History();
    }

    public Object getTickerObj(Class classObj) {

        //make our rest call to get our data
        String jsonResponse = JsonHelper.getJsonResponse(getTickerPriceUrl());

        //convert from json string to object
        return GsonHelper.getGson().fromJson(jsonResponse, classObj);
    }

    public Object getMarketDataObj(Class classObj) {

        //make our rest call to get our data
        String jsonResponse = JsonHelper.getJsonResponse(getDataFeedUrl());

        //convert from json string to object
        return GsonHelper.getGson().fromJson(jsonResponse, classObj);
    }

    public History getHistory() {
        return this.history;
    }

    public abstract void update();
}