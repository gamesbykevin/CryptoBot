package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.momentum.CommodityChannelIndex;

import java.util.List;

public class Strategy3 extends Strategy {

    //how we access our indicator(s)
    private final int indexCCI;

    //configure indicator
    private static final int PERIODS = 20;

    //signal values
    private static final float SIGNAL_BULLISH = 100;
    private static final float SIGNAL_BULLISH_HIGH = 200;
    private static final float SIGNAL_BEARISH = -100;

    protected Strategy3() {

        //call parent
        super(Key.Strategy_3);

        //add our indicator(s)
        this.indexCCI = addIndicator(new CommodityChannelIndex(PERIODS));
    }

    @Override
    public boolean hasSignalBuy(List<Candle> candles) {

        //get our indicator
        CommodityChannelIndex cci = (CommodityChannelIndex)getIndicator(this.indexCCI);

        //get our recent values
        double prev1 = getRecent(cci.getValues(), 2);
        double prev2 = getRecent(cci.getValues(), 3);
        double curr = getRecent(cci.getValues());

        if (prev2 > 0 && prev1 > 0 && prev1 < SIGNAL_BULLISH && curr >= SIGNAL_BULLISH)
            return true;

        //no signal
        return false;
    }

    @Override
    public boolean hasSignalSell(List<Candle> candles) {

        //get our indicator
        CommodityChannelIndex cci = (CommodityChannelIndex)getIndicator(this.indexCCI);

        //get our recent values
        double curr = getRecent(cci.getValues());

        //sell if we get too high
        if (curr >= SIGNAL_BULLISH_HIGH)
            return true;

        //if we touch 0, sell
        if (curr < 0)
            return true;

        //if we go below the bearish signal, exit the trade
        if (curr < SIGNAL_BEARISH)
            return true;

        //no signal
        return false;
    }
}