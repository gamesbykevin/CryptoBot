package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.trend.ExponentialMovingAverage;

import java.util.List;

public final class Strategy2 extends Strategy {

    //how we will access our indicators
    private final int indexEmaShort;
    private final int indexEmaLong;

    private static final int PERIODS_EMA_SHORT = 12;
    private static final int PERIODS_EMA_LONG = 26;

    protected Strategy2() {

        //set our strategy
        super(Key.Strategy_2);

        //add indicators and return their index position
        this.indexEmaShort = addIndicator(new ExponentialMovingAverage(PERIODS_EMA_SHORT, Fields.Close));
        this.indexEmaLong = addIndicator(new ExponentialMovingAverage(PERIODS_EMA_LONG, Fields.Close));
    }

    @Override
    public boolean hasSignalBuy(List<Candle> candles) {

        //get our indicators
        ExponentialMovingAverage emaShort = (ExponentialMovingAverage)getIndicator(indexEmaShort);
        ExponentialMovingAverage emaLong = (ExponentialMovingAverage)getIndicator(indexEmaLong);

        double emaShortCurrent = getRecent(emaShort.getValues());
        double emaShortPrevious = getRecent(emaShort.getValues(), 2);

        double emaLongCurrent = getRecent(emaLong.getValues());
        double emaLongPrevious = getRecent(emaLong.getValues(), 2);

        //if we just crossed above the long ema, we have a signal
        if (emaShortCurrent > emaLongCurrent && emaShortPrevious < emaLongPrevious)
            return true;

        return false;
    }

    @Override
    public boolean hasSignalSell(List<Candle> candles) {

        //get our indicators
        ExponentialMovingAverage emaShort = (ExponentialMovingAverage) getIndicator(indexEmaShort);
        ExponentialMovingAverage emaLong = (ExponentialMovingAverage) getIndicator(indexEmaLong);

        double emaShortCurrent = getRecent(emaShort.getValues());
        double emaLongCurrent = getRecent(emaLong.getValues());

        //if we went below the long ema, we have a signal
        if (emaShortCurrent < emaLongCurrent)
            return true;

        return false;
    }
}
