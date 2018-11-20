package com.gamesbykevin.cryptobot.broker;

import com.gamesbykevin.cryptobot.order.Order.Status;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.util.List;

import static com.gamesbykevin.cryptobot.order.Order.ATTEMPTS_LIMIT;
import static com.gamesbykevin.cryptobot.order.OrderHelper.getOrderDesc;
import static com.gamesbykevin.cryptobot.util.Properties.LIMIT_ORDERS;
import static com.gamesbykevin.cryptobot.util.Properties.PAPER_TRADING;
import static com.gamesbykevin.cryptobot.util.Util.NEW_LINE;

@Log4j
public class BrokerHelper {

    public static void checkOrder(Broker broker) throws Exception {

        //if we are paper trading
        if (PAPER_TRADING) {

            if (!LIMIT_ORDERS) {

                //if we are submitting market orders assume they are immediately filled
                broker.getOrder().setStatus(Status.Filled);

            } else {

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

                    default:
                        throw new Exception("Action not found: " + broker.getOrder().getAction());
                }
            }
        } else {

            if (!LIMIT_ORDERS) {

                //create market order

            } else {

                //create limit order
            }

            throw new Exception("Haven't implemented this yet");
        }

        //cancel the order if we reached the # of attempts and it's still pending
        if (broker.getOrder().getAttempts() > ATTEMPTS_LIMIT && broker.getOrder().getStatus() == Status.Pending)
            broker.getOrder().setStatus(Status.Cancelled);

        //display the progress
        log.info("Checking order: " + broker.getOrder().getStatus() + ", attempts = " + broker.getOrder().getAttempts());
        log.info(getOrderDesc(broker.getOrder()));
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

    public static String getBrokersDetails(List<Broker> brokers) {

        String desc = "";

        for (int index = 0; index < brokers.size(); index++) {

            Broker broker = brokers.get(index);

            //if we sold our stock at the current price, how much funds would we have?
            BigDecimal funds = broker.getFunds().add(broker.getQuantity().multiply(broker.getCalculator().getPrice()));

            desc += broker.getName() + " " + broker.getStrategy().getKey() + ", Available $" + broker.getFunds() + ", quantity: "  + broker.getQuantity() + ", Total Value $" + funds + ", " + broker.getCalculator().getDataFeedUrl();
            desc += NEW_LINE;
        }

        return desc;
    }
}