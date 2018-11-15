package com.gamesbykevin.cryptobot.broker;

import com.gamesbykevin.cryptobot.order.Order.Status;

import java.math.BigDecimal;

import static com.gamesbykevin.cryptobot.order.Order.ATTEMPTS_LIMIT;
import static com.gamesbykevin.cryptobot.util.Properties.PAPER_TRADING;
import static com.gamesbykevin.cryptobot.util.Util.display;

public class BrokerHelper {

    /**
     * When purchasing stock we round the quantity to make it simple
     */
    public static final int ROUND_DECIMALS_QUANTITY = 2;

    public static void checkOrder(Broker broker) {

        //if we are paper trading
        if (PAPER_TRADING) {

            //check if the order has filled
            switch (broker.getOrder().getAction()) {

                case Buy:

                    //if the stock price is less than our order price then our limit order has been filled
                    if (broker.getCalculator().getPrice().compareTo(broker.getOrder().getPrice()) == -1) {

                        //mark the status as filled
                        broker.getOrder().setStatus(Status.Filled);

                    } else {

                        //track the number of times we checked the order
                        broker.getOrder().setAttempts(broker.getOrder().getAttempts() + 1);
                    }

                    break;

                case Sell:

                    //if the stock price is more than our order price then our limit order has been filled
                    if (broker.getCalculator().getPrice().compareTo(broker.getOrder().getPrice()) == 1) {

                        //mark the status as filled
                        broker.getOrder().setStatus(Status.Filled);

                    } else {

                        //track the number of times we checked the order
                        broker.getOrder().setAttempts(broker.getOrder().getAttempts() + 1);
                    }

                    break;
            }
        }

        //cancel the order if we reached the # of attempts
        if (broker.getOrder().getAttempts() > ATTEMPTS_LIMIT)
            broker.getOrder().setStatus(Status.Cancelled);

        //display the progress
        display("Checking " + broker.getOrder().getAction() + " order: " + broker.getOrder().getStatus());
    }

    public static void fillOrder(Broker broker) {

        //fill the order
        switch (broker.getOrder().getAction()) {

            case Buy:

                //update our quantity
                broker.setQuantity(broker.getQuantity().add(broker.getOrder().getQuantity()));

                //how much did the order cost?
                BigDecimal purchasePrice = broker.getOrder().getPrice().multiply(broker.getOrder().getQuantity());

                //subtract the cost from our funds
                broker.setFunds(broker.getFunds().subtract(purchasePrice));
                break;

            case Sell:

                //update our quantity
                broker.setQuantity(broker.getQuantity().subtract(broker.getOrder().getQuantity()));

                //how much money did we end up with
                BigDecimal sellPrice = broker.getOrder().getPrice().multiply(broker.getOrder().getQuantity());

                //add the winnings to our total funds
                broker.setFunds(broker.getFunds().add(sellPrice));
                break;
        }

        //mark the order cancelled since we are now done
        broker.getOrder().setStatus(Status.Cancelled);
    }
}