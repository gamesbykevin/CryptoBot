package com.gamesbykevin.cryptobot.trade;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Trade {

    //the price we purchased
    private BigDecimal purchase;

    //the price we sold
    private BigDecimal sold;

    //quantity we bought / sold
    private BigDecimal quantity;

    //when was the trade started?
    private final long time;

    //what is the status of this trade
    private Status status = Status.Pending;

    public enum Status {
        Pending, Cancelled, Filled
    }
}