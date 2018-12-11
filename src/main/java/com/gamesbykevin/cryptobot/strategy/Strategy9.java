package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.trend.Envelopes;

import java.util.List;

public class Strategy9 extends Strategy {

    //how we access our indicator(s)
    private final int index;

    //configurable values
    private static final int PERIODS = 50;

    public Strategy9() {

        //call parent
        super(Key.Strategy_9);

        //add indicator
        this.index = addIndicator(new Envelopes(PERIODS));
    }

    @Override
    public boolean hasSignalBuy(List<Candle> candles) {

        //get our indicator
        Envelopes envelopes = (Envelopes)getIndicator(index);

        //get recent close $
        double price1 = candles.get(candles.size() - 1).getClose();
        double price2 = candles.get(candles.size() - 2).getClose();

        //if price goes below the lower then back above, we have a buy signal
        if (price2 < getRecent(envelopes.getLower(), 2) && price1 > getRecent(envelopes.getLower(), 1))
            return true;

        //no signal
        return false;
    }

    @Override
    public boolean hasSignalSell(List<Candle> candles) {

        //get our indicator
        Envelopes envelopes = (Envelopes)getIndicator(index);

        //get recent close $
        double price1 = candles.get(candles.size() - 1).getClose();
        double price2 = candles.get(candles.size() - 2).getClose();

        //if we are above the upper then go below, sell while we can
        if (price2 > getRecent(envelopes.getUpper(), 2) && price1 < getRecent(envelopes.getUpper(), 1))
            return true;


        //no signal
        return false;
    }
}