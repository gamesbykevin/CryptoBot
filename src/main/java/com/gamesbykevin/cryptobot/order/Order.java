package com.gamesbykevin.cryptobot.order;

import lombok.Data;

import java.math.BigDecimal;

import static com.gamesbykevin.cryptobot.util.Properties.getProperty;

@Data
public class Order {

    /**
     * How many attempts before we cancel the pending order
     */
    public static final int ATTEMPTS_LIMIT = Integer.parseInt(getProperty("attempts"));

    //what are we doing in this order
    private Action action;

    //size of the quantity
    private BigDecimal quantity;

    //what is the price
    private BigDecimal price;

    //how many times have we checked to see if this order has filled
    private int attempts = 0;

    //what is the status of this order
    private Status status = Status.Cancelled;

    public enum Status {

        //order is pending
        Pending,

        //order has been cancelled
        Cancelled,

        //order has been completely filled
        Filled,

        //order has been partially filled
        Partial
    }

    /**
     * What are we doing
     */
    public enum Action {
        Buy, Sell
    }
}