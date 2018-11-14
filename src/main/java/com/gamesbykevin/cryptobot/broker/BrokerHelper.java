package com.gamesbykevin.cryptobot.broker;

import com.gamesbykevin.cryptobot.trade.Trade;
import com.gamesbykevin.cryptobot.trade.Trade.Status;
import com.gamesbykevin.cryptobot.trade.TradeHelper;

import static com.gamesbykevin.cryptobot.util.Util.display;

public class BrokerHelper {

    /**
     * When purchasing stock we round the quantity to make it simple
     */
    public static final int ROUND_DECIMALS_QUANTITY = 2;

    /**
     * How many decimals do we round when talking about price
     */
    public static final int ROUND_DECIMALS_PRICE = 2;

    public static void purchase(Broker broker) {

        //create a new trade
        Trade trade = TradeHelper.create(broker.getFunds(), broker.getCalculator().getPrice());

        //deduct from our funds
        broker.setFunds(broker.getFunds().subtract(broker.getCalculator().getPrice().multiply(trade.getQuantity())));

        //add the trade to our list
        broker.getTrades().add(trade);

        //display info
        display("Trade purchase: " + broker.getCalculator().getPrice() + ", quantity: " + trade.getQuantity());
        display("Funds: $" + broker.getFunds());
    }

    public static void sell(Broker broker) {

        //get our pending trade
        Trade trade = getTradePending(broker);

        //assign the sale price
        trade.setSold(broker.getCalculator().getPrice());

        //mark trade as filled
        trade.setStatus(Trade.Status.Filled);

        //add the $ back to our funds
        broker.setFunds(broker.getFunds().add(broker.getCalculator().getPrice().multiply(trade.getQuantity())));
    }

    public static void cancel(Broker broker) {

        //cancel the pending trade
        getTradePending(broker).setStatus(Trade.Status.Cancelled);
    }

    /**
     * Get our pending trade, we can only have 1 pending trade at most
     * @return The pending trade, null if none found
     */
    public static Trade getTradePending(Broker broker) {

        //search for our pending trade
        for (int index = 0; index < broker.getTrades().size(); index++) {

            //get the current trade
            Trade trade = broker.getTrades().get(index);

            //if pending we found our trade
            if (trade.getStatus() == Status.Pending)
                return trade;
        }

        //no pending trades found
        return null;
    }
}