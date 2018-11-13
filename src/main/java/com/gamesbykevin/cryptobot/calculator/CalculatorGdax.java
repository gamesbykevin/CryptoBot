package com.gamesbykevin.cryptobot.calculator;

import com.gamesbykevin.cryptobot.util.GsonHelper;
import com.gamesbykevin.cryptobot.util.JsonHelper;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculatorGdax extends Calculator {

    /**
     * When we get our json data array what position is each attribute for?
     */
    public static final int PERIOD_INDEX_TIME = 0;
    public static final int PERIOD_INDEX_LOW = 1;
    public static final int PERIOD_INDEX_HIGH = 2;
    public static final int PERIOD_INDEX_OPEN = 3;
    public static final int PERIOD_INDEX_CLOSE = 4;
    public static final int PERIOD_INDEX_VOLUME = 5;

    public CalculatorGdax(final String dataFeedUrl, final String tickerPriceUrl) throws Exception {
        super(dataFeedUrl, tickerPriceUrl);
    }

    @Override
    public void update() {

        String jsonResponse = "";

        //make a call to get the current stock price
        jsonResponse = JsonHelper.getJsonResponse(getTickerPriceUrl());

        //store the current stock price
        setPrice(GsonHelper.getGson().fromJson(jsonResponse, Ticker.class).getPrice());

        System.out.println("Current Price of LTC $" + getPrice());

        //get json response
        jsonResponse = JsonHelper.getJsonResponse(getDataFeedUrl());

        //convert to array
        double[][] data = GsonHelper.getGson().fromJson(jsonResponse, double[][].class);

        //skip the first row because it contains data for the current candle that has not yet been formed
        for (int row = 1; row < data.length; row++) {

            getHistory().add(
                data[row][PERIOD_INDEX_OPEN],
                data[row][PERIOD_INDEX_CLOSE],
                data[row][PERIOD_INDEX_HIGH],
                data[row][PERIOD_INDEX_LOW],
                data[row][PERIOD_INDEX_VOLUME],
                (long)data[row][PERIOD_INDEX_TIME]
            );
        }
    }

    @Data
    private class Ticker {

        public String type;
        public long sequence;
        public String product_id;
        public BigDecimal price;
        public double open_24h;
        public double volume_24h;
        public double low_24h;
        public double high_24h;
        public double volume_30d;
        public double best_bid;
        public double best_ask;
    }
}