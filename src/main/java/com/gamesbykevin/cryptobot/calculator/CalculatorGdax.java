package com.gamesbykevin.cryptobot.calculator;

import com.gamesbykevin.cryptobot.util.Util;
import lombok.Data;

import java.math.BigDecimal;

import static com.gamesbykevin.cryptobot.util.Util.FILE_SEPARATOR;

@Data
public final class CalculatorGdax extends Calculator {

    /**
     * When we get our json data array what position is each attribute for?
     */
    public static final int PERIOD_INDEX_TIME = 0;
    public static final int PERIOD_INDEX_LOW = 1;
    public static final int PERIOD_INDEX_HIGH = 2;
    public static final int PERIOD_INDEX_OPEN = 3;
    public static final int PERIOD_INDEX_CLOSE = 4;
    public static final int PERIOD_INDEX_VOLUME = 5;

    protected CalculatorGdax(final String dataFeedUrl, final String tickerPriceUrl) throws Exception {
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

            //identify the stock
            String product = directories[4].replaceAll("-", "_");

            //identify the candle size
            String candle = Util.parseUrl(getDataFeedUrl()).get("granularity");

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