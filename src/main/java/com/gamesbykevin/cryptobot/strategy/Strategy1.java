package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.trend.SimpleMovingAverage;

public final class Strategy1 extends Strategy {

    //how we will access our indicators
    private final int indexSmaShort;
    private final int indexSmaLong;

    private static final int PERIODS_SMA_SHORT = 10;
    private static final int PERIODS_SMA_LONG = 50;

    protected Strategy1() {

        //set our strategy
        super(Key.Strategy_1);

        //add indicators and return their index position
        this.indexSmaShort = addIndicator(new SimpleMovingAverage(PERIODS_SMA_SHORT, Fields.Close));
        this.indexSmaLong = addIndicator(new SimpleMovingAverage(PERIODS_SMA_LONG, Fields.Close));
    }

    @Override
    public boolean hasSignalBuy() {

        //get our indicators
        SimpleMovingAverage smaShort = (SimpleMovingAverage)getIndicator(indexSmaShort);
        SimpleMovingAverage smaLong = (SimpleMovingAverage)getIndicator(indexSmaLong);

        double smaShort1 = getRecent(smaShort.getValues());
        double smaShort2 = getRecent(smaShort.getValues(), 2);

        double smaLong1 = getRecent(smaLong.getValues());
        double smaLong2 = getRecent(smaLong.getValues(), 2);

        //if we just crossed above the long sma, we have a signal
        if (smaShort1 > smaLong1 && smaShort2 < smaLong2)
            return true;

        return true;
    }

    @Override
    public boolean hasSignalSell() {

        //get our indicators
        SimpleMovingAverage smaShort = (SimpleMovingAverage)getIndicator(indexSmaShort);
        SimpleMovingAverage smaLong = (SimpleMovingAverage)getIndicator(indexSmaLong);

        double smaShort1 = getRecent(smaShort.getValues());
        double smaShort2 = getRecent(smaShort.getValues(), 2);

        double smaLong1 = getRecent(smaLong.getValues());
        double smaLong2 = getRecent(smaLong.getValues(), 2);

        //if we just crossed below the long sma, we have a signal
        if (smaShort1 < smaLong1 && smaShort2 > smaLong2)
            return true;

        return false;
    }
}