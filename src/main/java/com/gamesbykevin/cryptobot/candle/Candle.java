package com.gamesbykevin.cryptobot.candle;

import lombok.Data;

@Data
public class Candle {

    /**
     * Fields we can calculate in this class
     */
    public enum Fields {
        Open,
        Close,
        High,
        Low,
        Volume
    }

    //what value did we open
    private double open;

    //what value did we close
    private double close;

    //what was the high
    private double high;

    //what was the low
    private double low;

    //volume of the candle
    private double volume;

    //what time is this candle for
    private long time;
}