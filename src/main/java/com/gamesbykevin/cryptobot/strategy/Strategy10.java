package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.momentum.TrueStrengthIndex;

import java.util.List;

public class Strategy10 extends Strategy {

    //how we access our indicator(s)
    private final int index;

    //configurable values
    private static final int PERIODS = 25, PERIODS_SHORT = 13, PERIODS_SIGNAL = 7;

    private static final float OVERSOLD = -25.0f, OVERBOUGHT = 25.0f;

    public Strategy10() {

        //call parent
        super(Key.Strategy_10);

        //add indicator
        this.index = addIndicator(new TrueStrengthIndex(PERIODS, PERIODS_SHORT, PERIODS_SIGNAL));
    }

    @Override
    public boolean hasSignalBuy(List<Candle> candles) {

        //get our indicator
        TrueStrengthIndex indicator = (TrueStrengthIndex)getIndicator(index);

        if (getRecent(indicator.getValues()) <= OVERSOLD)
            return true;

        //no signal
        return false;
    }

    @Override
    public boolean hasSignalSell(List<Candle> candles) {

        //get our indicator
        TrueStrengthIndex indicator = (TrueStrengthIndex)getIndicator(index);

        if (getRecent(indicator.getValues()) >= OVERBOUGHT)
            return true;

        //no signal
        return false;
    }
}