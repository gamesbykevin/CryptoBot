package com.gamesbykevin.cryptobot.broker;

import com.gamesbykevin.cryptobot.calculator.CalculatorHelper;
import com.gamesbykevin.cryptobot.strategy.StrategyHelper;
import com.gamesbykevin.cryptobot.util.Properties;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.cryptobot.broker.BrokerHelper.getBrokersDetails;
import static com.gamesbykevin.cryptobot.util.Email.EMAIL_NOTIFICATION_DELAY;
import static com.gamesbykevin.cryptobot.util.Email.send;
import static com.gamesbykevin.cryptobot.util.Properties.*;

@Log4j
@Data
public class BrokerManager extends Thread implements Runnable {

    /**
     * How long do we sleep in between each broker update
     */
    public static final long SLEEP = Long.parseLong(getProperty("sleep"));

    /**
     * How much total $ do we have to start
     */
    public static final BigDecimal FUNDS = new BigDecimal(getProperty("funds"));

    //list of brokers trading stock
    private List<Broker> brokers;

    public BrokerManager() {

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
            BigDecimal share = FUNDS.divide(total, RoundingMode.DOWN);

            //we will create a broker for every strategy and data feed combination
            for (int i = 0; i < STRATEGIES.length; i++) {
                for (int j = 0; j < DATA_FEED_URL.length; j++) {

                    //create a new broker
                    Broker broker = new Broker();

                    //each broker will have a unique name
                    broker.setName("Broker " + this.brokers.size());

                    //assign their share of the funds
                    broker.setFunds(share);

                    //assign the calculator as well
                    broker.setCalculator(CalculatorHelper.create(DATA_FEED_URL[j], TICKER_PRICE_URL[j]));

                    //our broker needs a strategy in order to trade
                    broker.setStrategy(StrategyHelper.create(STRATEGIES[i]));

                    //add to our list
                    this.brokers.add(broker);

                    //print to console
                    log.info("Created broker $" + share + ", " + STRATEGIES[i] + ", " + DATA_FEED_URL[j]);
                }
            }

            long time = System.currentTimeMillis();

            while (true) {

                //update the brokers
                for (int index = 0; index < this.brokers.size(); index++) {

                    try {

                        //update the current broker
                        this.brokers.get(index).update();

                    } catch (Exception ex) {

                        //print error trace
                        log.error(ex.getMessage(), ex);
                    }

                    //sleep
                    Thread.sleep(SLEEP);
                }

                if (System.currentTimeMillis() - time >= EMAIL_NOTIFICATION_DELAY) {

                    send("CryptoBot Update", getBrokersDetails(this.brokers));
                    time = System.currentTimeMillis();

                } else {

                    long seconds = (EMAIL_NOTIFICATION_DELAY - (System.currentTimeMillis() - time)) / 1000;
                    log.info("Next update in " + seconds + " seconds");
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}