package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.volatility.BollingerBands;

import java.util.List;

public class Strategy8 extends Strategy {

    //how we access our indicator(s)
    private final int indexBB;

    //configurable values
    private static final int PERIODS = 20;

    public Strategy8() {

        //call parent
        super(Key.Strategy_8);

        //add indicator
        this.indexBB = addIndicator(new BollingerBands(PERIODS));
    }

    @Override
    public boolean hasSignalBuy(List<Candle> candles) {

        //get our indicator
        BollingerBands bb = (BollingerBands)getIndicator(indexBB);

        //if the recent close was above and then goes below the lower band, let's buy
        if (candles.get(candles.size() - 2).getClose() > getRecent(bb.getLower(), 2) && candles.get(candles.size() - 1).getClose() <= getRecent(bb.getLower()))
            return true;

        //no signal
        return false;
    }

    @Override
    public boolean hasSignalSell(List<Candle> candles) {

        //get our indicator
        BollingerBands bb = (BollingerBands)getIndicator(indexBB);

        //if the recent close goes above the upper band, let's sell
        if (candles.get(candles.size() - 1).getClose() >= getRecent(bb.getUpper()))
            return true;

        //get the recent close $
        double closePrev1 = candles.get(candles.size() - 1).getClose();
        double closePrev2 = candles.get(candles.size() - 2).getClose();
        double closePrev3 = candles.get(candles.size() - 3).getClose();

        //if close was above the middle band then went back below let's sell
        if (closePrev3 > getRecent(bb.getMiddle(), 3) && closePrev2 < getRecent(bb.getMiddle(), 2) && closePrev1 < getRecent(bb.getMiddle()))
            return true;

        //no signal
        return false;
    }
}