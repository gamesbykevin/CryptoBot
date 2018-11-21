package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.indicator.trend.MovingAverageConvergenceDivergence;

public class Strategy5 extends Strategy {

    //how we access our indicator(s)
    private final int indexMacd;

    //configurable values
    private static final int PERIODS_EMA_SHORT = 12;
    private static final int PERIODS_EMA_LONG = 26;
    private static final int PERIODS_SIGNAL = 9;

    protected Strategy5() throws Exception {

        //call parent
        super(Key.Strategy_5);

        //add our indicator(s)
        this.indexMacd = addIndicator(new MovingAverageConvergenceDivergence(PERIODS_EMA_SHORT, PERIODS_EMA_LONG, PERIODS_SIGNAL));
    }

    @Override
    public boolean hasSignalBuy() {

        //get our indicator
        MovingAverageConvergenceDivergence macd = (MovingAverageConvergenceDivergence)getIndicator(indexMacd);

        if (getRecent(macd.getValues()) > getRecent(macd.getSignalLine()) && getRecent(macd.getValues()) > 0)
            return true;

        return false;
    }

    @Override
    public boolean hasSignalSell() {

        //get our indicator
        MovingAverageConvergenceDivergence macd = (MovingAverageConvergenceDivergence)getIndicator(indexMacd);

        if (getRecent(macd.getValues()) > getRecent(macd.getSignalLine()))
            return true;

        return false;
    }
}
