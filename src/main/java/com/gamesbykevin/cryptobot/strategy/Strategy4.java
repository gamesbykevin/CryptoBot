package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.indicator.momentum.RelativeStrengthIndex;

public class Strategy4 extends Strategy {

    //how we access our indicator(s)
    private final int indexRSI;

    //configurable values
    private static final int PERIODS = 14;
    private static final double OVERBOUGHT = 70d;
    private static final double OVERSOLD = 30d;

    protected Strategy4() {

        //call parent
        super(Key.Strategy_4);

        //add our indicator(s)
        this.indexRSI = addIndicator(new RelativeStrengthIndex(PERIODS));
    }

    @Override
    public boolean hasSignalBuy() {

        //get our indicator
        RelativeStrengthIndex rsi = (RelativeStrengthIndex)getIndicator(indexRSI);

        if (getRecent(rsi.getValues()) < OVERSOLD)
            return true;

        return false;
    }

    @Override
    public boolean hasSignalSell() {

        //get our indicator
        RelativeStrengthIndex rsi = (RelativeStrengthIndex)getIndicator(indexRSI);

        if (getRecent(rsi.getValues()) > OVERBOUGHT)
            return true;

        return false;
    }
}
