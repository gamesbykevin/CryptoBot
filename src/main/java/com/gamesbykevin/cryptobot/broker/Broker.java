package com.gamesbykevin.cryptobot.broker;

import com.gamesbykevin.cryptobot.calculator.Calculator;
import com.gamesbykevin.cryptobot.strategy.Strategy;
import com.gamesbykevin.cryptobot.trade.Trade;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.cryptobot.broker.BrokerHelper.getTradePending;
import static com.gamesbykevin.cryptobot.broker.BrokerHelper.purchase;
import static com.gamesbykevin.cryptobot.broker.BrokerHelper.sell;
import static com.gamesbykevin.cryptobot.util.Util.display;

@Data
public class Broker {

    //the amount of funds the broker has
    private BigDecimal funds;

    //list of trades tied to the broker (includes pending, cancelled, & filled)
    private List<Trade> trades;

    //do we stop trading?
    private boolean stop = false;

    //the trading strategy this broker is usin g
    private Strategy strategy;

    //the calculator which will contain our market data
    private Calculator calculator;

    public Broker() {

        //create a new list to hold our trades
        this.trades = new ArrayList<>();
    }

    public void update() {

        //get the newest timestamp
        final long beforeTime = getCalculator().getHistory().getRecent();

        //get the current $ of our stock
        final BigDecimal beforePrice = getCalculator().getPrice();

        //update the calculator
        getCalculator().update();

        //get the price after
        final BigDecimal afterPrice = getCalculator().getPrice();

        //if the price changed display it
        if (beforePrice == null && afterPrice != null || !beforePrice.equals(afterPrice))
            display("Current Price $" + getCalculator().getPrice() + ", " + getCalculator().getTickerPriceUrl());

        //get the newest timestamp
        final long afterTime = getCalculator().getHistory().getRecent();

        //if the time is different we have new data
        if (beforeTime != afterTime) {

            //display what we are calculating
            display("Calculating: " + getStrategy().getKey() + "(" + getCalculator().getDataFeedUrl() + ")");

            //perform calculations with our data
            getStrategy().calculate(getCalculator().getHistory().getCandles());
        }

        //if there is no pending trade, let's see if we can buy
        if (getTradePending(this) == null) {

            //do we have a signal to buy?
            if (getStrategy().hasSignalBuy()) {
                purchase(this);
            }

        } else {

            //do we have a signal to sell our pending trade?
            if (getStrategy().hasSignalSell()) {
                sell(this);
            }

        }
    }
}