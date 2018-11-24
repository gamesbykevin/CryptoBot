package com.gamesbykevin.cryptobot.calculator;

import com.gamesbykevin.cryptobot.util.Util;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

import static com.gamesbykevin.cryptobot.util.Util.FILE_SEPARATOR;

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

    protected CalculatorBinance(final String dataFeedUrl, final String tickerPriceUrl) throws Exception {
        super(dataFeedUrl, tickerPriceUrl);
    }

    @Override
    public void calculateHistoryFilePath() throws Exception {

        //create our directory if we haven't yet
        if (getDirectory() == null) {

            //split the data to construct our directories
            String[] directories = getDataFeedUrl().split("/");

            //identify our parent directory
            String parent = directories[2].replaceAll("\\.", "_");

            //parse the url to get the other data
            Map<String, String> params = Util.parseUrl(getDataFeedUrl());

            //identify the stock
            String product = params.get("symbol");

            //identify the candle size
            String candle = params.get("interval");

            //now we can construct our file path
            setDirectory(parent + FILE_SEPARATOR + product + FILE_SEPARATOR + candle);
        }
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