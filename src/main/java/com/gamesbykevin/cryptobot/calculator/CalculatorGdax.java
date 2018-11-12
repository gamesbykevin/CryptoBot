package com.gamesbykevin.cryptobot.calculator;

import com.gamesbykevin.cryptobot.broker.Broker;
import com.gamesbykevin.cryptobot.strategy.Strategy;
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

    //the current price of what we are trading
    private BigDecimal price;

    public CalculatorGdax() throws Exception {
        super();
    }

    @Override
    public void update(Broker broker) {

        //get the time of the latest candle
        final long time = getHistory().getRecent();

        String jsonResponse = "";

        //make a call to get the current stock price
        jsonResponse = JsonHelper.getJsonResponse("https://api.gdax.com/products/LTC-USD/ticker");

        //store the current stock price
        setPrice(GsonHelper.getGson().fromJson(jsonResponse, Ticker.class).price);

        System.out.println("Current Price of LTC $" + getPrice());

        //get json response
        //jsonResponse = JsonHelper.getJsonResponse("https://api.gdax.com/products/LTC-USD/candles?granularity=900");
        jsonResponse = JsonHelper.getJsonResponse("https://api.gdax.com/products/LTC-USD/candles?granularity=60");

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

        //if new time was discovered we need to re-calculate
        if (time != getHistory().getRecent()) {

            for (int index = 0; index < getStrategies().size(); index++) {

                Strategy strategy = getStrategy(index);

                strategy.calculate(getHistory().getCandles());
                strategy.display();

                if (strategy.hasSignalBuy()) {
                    broker.purchase(getPrice());
                    System.out.println("Buy");
                } else if (broker.getTradePending() != null && strategy.hasSignalSell()) {
                    broker.sell(getPrice());
                    System.out.println("Sell");
                }
            }
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