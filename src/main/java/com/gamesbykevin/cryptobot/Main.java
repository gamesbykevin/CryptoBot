package com.gamesbykevin.cryptobot;

import com.gamesbykevin.cryptobot.broker.Broker;
import com.gamesbykevin.cryptobot.calculator.Calculator;
import com.gamesbykevin.cryptobot.calculator.CalculatorGdax;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import com.gamesbykevin.cryptobot.strategy.Strategy;
import com.gamesbykevin.cryptobot.strategy.StrategyHelper;
import com.gamesbykevin.cryptobot.util.Util;

import java.math.BigDecimal;

import static com.gamesbykevin.cryptobot.broker.Broker.ROUND_DECIMALS_PRICE;

public class Main extends Thread implements Runnable {

    public static void main(String[] args) {

	    // write your code here
        Main main = new Main();
        main.start();
    }

    public Main() {
        //default constructor
    }

    @Override
    public void run() {

        try {

            Broker broker = new Broker();
            broker.setFunds(Util.round(ROUND_DECIMALS_PRICE, BigDecimal.valueOf(1000L)));

            Calculator calculator = new CalculatorGdax();

            while (true) {

                //update our calculations
                calculator.update(broker);

                //sleep
                System.out.println("Sleeping " + System.currentTimeMillis());
                Thread.sleep(1000L);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}