package com.gamesbykevin.cryptobot.calculator;

import lombok.Data;

import java.math.BigDecimal;

import static com.gamesbykevin.cryptobot.util.Util.display;

public final class CalculatorBinance extends Calculator {

    /**
     * When we get our json data array what position is each attribute for?
     */
    public static final int PERIOD_INDEX_OPEN = 1;
    public static final int PERIOD_INDEX_HIGH = 2;
    public static final int PERIOD_INDEX_LOW = 3;
    public static final int PERIOD_INDEX_CLOSE = 4;
    public static final int PERIOD_INDEX_VOLUME = 5;
    public static final int PERIOD_INDEX_TIME = 6;

    protected CalculatorBinance(final String dataFeedUrl, final String tickerPriceUrl) {
        super(dataFeedUrl, tickerPriceUrl);
    }

    @Override
    public void update() {

        //get our ticker data
        Ticker ticker = (Ticker)getTickerObj(Ticker.class);

        //store the current stock price
        setPrice(ticker.getPrice());

        //get our market data
        double[][] data = (double[][])getMarketDataObj(double[][].class);

        //skip the first row because it contains data for the current candle that has not yet been formed
        for (int row = 0; row < data.length; row++) {

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
        private String symbol;
        private BigDecimal price;
    }
}