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

        double smaShortCurrent = getRecent(smaShort.getValues());
        double smaShortPrevious = getRecent(smaShort.getValues(), 2);

        double smaLongCurrent = getRecent(smaLong.getValues());
        double smaLongPrevious = getRecent(smaLong.getValues(), 2);

        //if we just crossed above the long sma, we have a signal
        if (smaShortCurrent > smaLongCurrent)// && smaShortPrevious < smaLongPrevious)
            return true;

        return false;
    }

    @Override
    public boolean hasSignalSell() {

        //get our indicators
        SimpleMovingAverage smaShort = (SimpleMovingAverage)getIndicator(indexSmaShort);
        SimpleMovingAverage smaLong = (SimpleMovingAverage)getIndicator(indexSmaLong);

        double smaShortCurrent = getRecent(smaShort.getValues());
        double smaLongCurrent = getRecent(smaLong.getValues());

        //if we went below the long sma, we have a signal
        if (smaShortCurrent < smaLongCurrent)
            return true;

        return false;
    }
}