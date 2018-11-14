package com.gamesbykevin.cryptobot.calculator;

public class CalculatorHelper {

    public static final String URL_CONTAINS_BINANCE = "BINANCE";
    public static final String URL_CONTAINS_GDAX = "GDAX";

    public static Calculator create(final String dataFeedUrl, final String tickerPriceUrl) {

        if (dataFeedUrl == null)
            return null;

        Calculator calculator = null;

        if (dataFeedUrl.toUpperCase().contains(URL_CONTAINS_BINANCE)) {
            calculator = new CalculatorBinance(dataFeedUrl, tickerPriceUrl);
        } else if (dataFeedUrl.toUpperCase().contains(URL_CONTAINS_GDAX)) {
            calculator = new CalculatorGdax(dataFeedUrl, tickerPriceUrl);
        }

        return calculator;
    }
}