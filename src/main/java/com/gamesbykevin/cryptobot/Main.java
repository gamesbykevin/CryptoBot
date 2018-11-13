package com.gamesbykevin.cryptobot;

import com.gamesbykevin.cryptobot.broker.Broker;
import com.gamesbykevin.cryptobot.calculator.CalculatorGdax;
import com.gamesbykevin.cryptobot.strategy.Strategy;
import com.gamesbykevin.cryptobot.strategy.StrategyHelper;
import com.gamesbykevin.cryptobot.util.Properties;
import com.gamesbykevin.cryptobot.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.cryptobot.broker.BrokerHelper.ROUND_DECIMALS_PRICE;
import static com.gamesbykevin.cryptobot.util.Properties.DATA_FEED_URL;
import static com.gamesbykevin.cryptobot.util.Properties.STRATEGIES;
import static com.gamesbykevin.cryptobot.util.Properties.TICKER_PRICE_URL;

public class Main extends Thread implements Runnable {

    public static void main(String[] args) {

	    // write your code here
        Main main = new Main();
        main.start();
    }

    //list of brokers trading stock
    private List<Broker> brokers;

    public Main() {

        //create new list of brokers
        this.brokers = new ArrayList<>();
    }

    @Override
    public void run() {

        try {

            //we need a ticker url for every data feed
            if (DATA_FEED_URL.length != TICKER_PRICE_URL.length)
                throw new Exception("Length of data feed (" + DATA_FEED_URL.length + ") and ticker price (" + TICKER_PRICE_URL.length + ") have to match");

            //how many different combinations do we have?
            BigDecimal total = BigDecimal.valueOf(DATA_FEED_URL.length * STRATEGIES.length);

            //each broker will get an equal share of the total funds
            BigDecimal share = new BigDecimal(Properties.getProperty("funds")).divide(total, RoundingMode.HALF_DOWN);

            //we will create a broker for every strategy and data feed combination
            for (int i = 0; i < STRATEGIES.length; i++) {
                for (int j = 0; j < DATA_FEED_URL.length; j++) {

                    //create a new broker
                    Broker broker = new Broker();

                    //assign their share of the funds
                    broker.setFunds(share);

                    //assign the calculator as well
                    broker.setCalculator(new CalculatorGdax(DATA_FEED_URL[j], TICKER_PRICE_URL[j]));

                    //our broker needs a strategy in order to trade
                    broker.setStrategy(StrategyHelper.create(STRATEGIES[i]));

                    System.out.println("Created broker $" + share + ", " + STRATEGIES[i] + ", " + DATA_FEED_URL[j]);

                    //add to our list
                    this.brokers.add(broker);
                }
            }

            while (true) {

                //update the brokers
                for (int index = 0; index < this.brokers.size(); index++) {
                    this.brokers.get(index).update();
                }

                //sleep
                System.out.println("Sleeping " + System.currentTimeMillis());
                Thread.sleep(1000L);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}