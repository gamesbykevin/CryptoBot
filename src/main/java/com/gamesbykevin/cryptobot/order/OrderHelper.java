package com.gamesbykevin.cryptobot.order;

import com.gamesbykevin.cryptobot.broker.Broker;
import com.gamesbykevin.cryptobot.order.Order.Action;
import com.gamesbykevin.cryptobot.order.Order.Status;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.gamesbykevin.cryptobot.broker.BrokerHelper.ROUND_DECIMALS_QUANTITY;
import static com.gamesbykevin.cryptobot.util.Util.round;

public class OrderHelper {

    /**
     * Create a penny
     */
    public static final BigDecimal PENNY = new BigDecimal("0.01");

    public static void createOrderBuy(Broker broker) {

        //buy order
        broker.getOrder().setAction(Action.Buy);

        //we will subtract a penny from the current price
        broker.getOrder().setPrice(broker.getCalculator().getPrice().subtract(PENNY));

        //determine how much quantity we can buy
        BigDecimal quantity = broker.getFunds().divide(broker.getOrder().getPrice(), RoundingMode.HALF_DOWN);

        //round our quantity to a nice number
        broker.getOrder().setQuantity(round(ROUND_DECIMALS_QUANTITY, quantity));

        //new orders are always pending
        broker.getOrder().setStatus(Status.Pending);

        //reset the # of attempts
        broker.getOrder().setAttempts(0);
    }

    public static void createOrderSell(Broker broker) {

        //sell order
        broker.getOrder().setAction(Action.Sell);

        //we will add a penny to the current price
        broker.getOrder().setPrice(broker.getCalculator().getPrice().add(PENNY));

        //we are selling all quantity
        broker.getOrder().setQuantity(broker.getQuantity());

        //new orders are always pending
        broker.getOrder().setStatus(Status.Pending);

        //reset the # of attempts
        broker.getOrder().setAttempts(0);
    }
}