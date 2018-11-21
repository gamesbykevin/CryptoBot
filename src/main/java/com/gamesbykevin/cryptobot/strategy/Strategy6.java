package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.indicator.momentum.StochasticOscillator;

public class Strategy6 extends Strategy {

    //how we access our indicator(s)
    private final int indexSO;

    //configurable values
    private static final int PERIODS_K = 14;
    private static final int PERIODS_K_SMA = 3;
    private static final int PERIODS_D_SMA = 3;
    private static final double OVERSOLD = 20d;
    private static final double OVERBOUGHT = 80d;

    public Strategy6() {

        //call parent
        super(Key.Strategy_6);

        //add indicator
        this.indexSO = addIndicator(new StochasticOscillator(PERIODS_K, PERIODS_K_SMA, PERIODS_D_SMA));
    }

    @Override
    public boolean hasSignalBuy() {

        //get our indicator
        StochasticOscillator so = (StochasticOscillator)getIndicator(this.indexSO);

        if (getRecent(so.getFastK()) < OVERSOLD) {

            if (getRecent(so.getSlowK(), 2) < getRecent(so.getSlowD(), 2) &&
                    getRecent(so.getSlowK()) > getRecent(so.getSlowD()))
                return true;
        }

        //no signal
        return false;
    }

    @Override
    public boolean hasSignalSell() {

        //get our indicator
        StochasticOscillator so = (StochasticOscillator)getIndicator(this.indexSO);

        if (getRecent(so.getFastK()) > OVERBOUGHT) {
            if (getRecent(so.getSlowK()) < getRecent(so.getSlowD()))
                return true;
        }

        //no signal
        return false;
    }
}