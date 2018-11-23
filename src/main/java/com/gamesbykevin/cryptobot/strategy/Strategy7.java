package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.momentum.MoneyFlowIndex;

import java.util.List;

public class Strategy7 extends Strategy {

    //how we access our indicator(s)
    private final int indexMFI;

    //configurable values
    private static final int PERIODS = 14;
    private static final double OVERSOLD = 10d;
    private static final double OVERBOUGHT = 90d;

    public Strategy7() {

        //call parent
        super(Key.Strategy_7);

        //add indicator
        this.indexMFI = addIndicator(new MoneyFlowIndex(PERIODS));
    }

    @Override
    public boolean hasSignalBuy(List<Candle> candles) {

        //get our indicator
        MoneyFlowIndex mfi = (MoneyFlowIndex)getIndicator(this.indexMFI);

        if (getRecent(mfi.getValues()) < OVERSOLD)
            return true;

        //no signal
        return false;
    }

    @Override
    public boolean hasSignalSell(List<Candle> candles) {

        //get our indicator
        MoneyFlowIndex mfi = (MoneyFlowIndex)getIndicator(this.indexMFI);

        if (getRecent(mfi.getValues()) > OVERBOUGHT)
            return true;

        //no signal
        return false;
    }
}