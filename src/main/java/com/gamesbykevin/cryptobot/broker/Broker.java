package com.gamesbykevin.cryptobot.broker;

import com.gamesbykevin.cryptobot.calculator.Calculator;
import com.gamesbykevin.cryptobot.order.Order;
import com.gamesbykevin.cryptobot.order.Order.Status;
import com.gamesbykevin.cryptobot.strategy.Strategy;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;

import static com.gamesbykevin.cryptobot.broker.BrokerHelper.checkOrder;
import static com.gamesbykevin.cryptobot.broker.BrokerHelper.fillOrder;
import static com.gamesbykevin.cryptobot.order.OrderHelper.createOrderBuy;
import static com.gamesbykevin.cryptobot.order.OrderHelper.createOrderSell;

@Data
@Log4j
public class Broker {

    //the amount of funds the broker has
    private BigDecimal funds;

    //how much quantity do we have right now starting at 0
    private BigDecimal quantity = BigDecimal.ZERO;

    //do we stop trading?
    private boolean stop = false;

    //current pending order
    private Order order;

    //the trading strategy this broker is using
    private Strategy strategy;

    //the calculator which will contain our market data
    private Calculator calculator;

    //each broker will have a name
    private String name;

    public void update() throws Exception {

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
            log.info("Key: " + getStrategy().getKey() + ", Current Price $" + getCalculator().getPrice() + ", Quantity: " + getQuantity() + ", " + getCalculator().getDataFeedUrl());

        //get the newest timestamp
        final long afterTime = getCalculator().getHistory().getRecent();

        //if the time is different we have new data
        if (beforeTime != afterTime) {

            //display what we are calculating
            log.info("Calculating: " + getStrategy().getKey() + "(" + getCalculator().getDataFeedUrl() + ")");

            //perform calculations with our data
            getStrategy().calculate(getCalculator().getHistory().getCandles());
        }

        switch (getOrder().getStatus()) {

            //if the order is cancelled let's see if we can buy or sell
            case Cancelled:

                //if the quantity is greater than 0 let's look for an opportunity to sell
                if (getQuantity().compareTo(BigDecimal.ZERO) > 0) {

                    //if we have a signal, create the order
                    if (getStrategy().hasSignalSell())
                        createOrderSell(this);

                } else {

                    //if we have a signal, create the order
                    if (getStrategy().hasSignalBuy())
                        createOrderBuy(this);
                }

                break;

            //if we have a pending order, check it
            case Pending:
                checkOrder(this);
                break;

            //if the order has been filled or partially filled let's update
            case Filled:
            case Partial:
                fillOrder(this);
                break;
        }
    }

    public Order getOrder() {

        if (this.order == null) {
            this.order = new Order();
            this.order.setStatus(Status.Cancelled);
        }

        return this.order;
    }
}