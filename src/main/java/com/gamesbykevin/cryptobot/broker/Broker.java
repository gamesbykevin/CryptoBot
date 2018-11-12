package com.gamesbykevin.cryptobot.broker;

import com.gamesbykevin.cryptobot.trade.Trade;
import com.gamesbykevin.cryptobot.trade.TradeHelper;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Broker {

    //the amount of funds the broker has
    private BigDecimal funds;

    //list of trades this broker has made
    private List<Trade> trades;

    //do we stop trading?
    private boolean stop = false;

    /**
     * When purchasing stock we round the quantity to make it simple
     */
    public static final int ROUND_DECIMALS_QUANTITY = 2;

    /**
     * How many decimals do we round when talking about price
     */
    public static final int ROUND_DECIMALS_PRICE = 2;

    public Broker() {
        this.trades = new ArrayList<>();
    }

    public void purchase(BigDecimal price) {

        //create a new trade
        Trade trade = TradeHelper.create(getFunds(), price);

        //deduct from our funds
        setFunds(getFunds().subtract(price.multiply(trade.getQuantity())));

        //add the trade to our list
        getTrades().add(trade);

        //display info
        System.out.println("Trade purchase: " + price + ", quantity: " + trade.getQuantity());
        System.out.println("Funds: $" + getFunds());
    }

    public void sell(BigDecimal price) {

        //get our pending trade
        Trade trade = getTradePending();

        //assign the sale price
        trade.setSold(price);

        //mark trade as filled
        trade.setStatus(Trade.Status.Filled);

        //add the $ back to our funds
        setFunds(getFunds().add(price.multiply(trade.getQuantity())));
    }

    public void cancel() {

        //cancel the pending trade
        getTradePending().setStatus(Trade.Status.Cancelled);
    }

    /**
     * Get our pending trade, we can only have 1 pending trade at most
     * @return The pending trade, null if none found
     */
    public Trade getTradePending() {

        //search for our pending trade
        for (int index = 0; index < getTrades().size(); index++) {

            //get the current trade
            Trade trade = getTrades().get(index);

            //if pending we found our trade
            if (trade.getStatus() == Trade.Status.Pending)
                return trade;
        }

        //no pending trades found
        return null;
    }
}