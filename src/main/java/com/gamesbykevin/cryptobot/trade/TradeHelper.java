package com.gamesbykevin.cryptobot.trade;

import com.gamesbykevin.cryptobot.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.gamesbykevin.cryptobot.broker.BrokerHelper.ROUND_DECIMALS_QUANTITY;

public class TradeHelper {

    public static Trade create(BigDecimal funds, BigDecimal price) {

        //create trade
        Trade trade = new Trade(System.currentTimeMillis());

        //new trades are pending
        trade.setStatus(Trade.Status.Pending);

        //assign the trade price
        trade.setPurchase(price);

        //determine how much we can buy
        BigDecimal quantity = funds.divide(price, RoundingMode.HALF_DOWN);

        //round to a nice number
        quantity = Util.round(ROUND_DECIMALS_QUANTITY, quantity);

        //assign the quantity
        trade.setQuantity(quantity);

        //return our trade
        return trade;
    }
}