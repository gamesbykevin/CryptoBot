package com.gamesbykevin.cryptobot.indicator.volume;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.List;

public class AccumulationDistributionLine extends Indicator {

    public AccumulationDistributionLine() {
        super(Key.AccumulationDistributionLine, 0, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear values
        getValues().clear();

        for (int index = 0; index < candles.size(); index++) {

            Candle candle = candles.get(index);

            //figure out the multiplier
            double multiplier = getMultiplier(candle);

            //and now our volume
            double volume = multiplier * candle.getVolume();

            if (getValues().isEmpty()) {
                getValues().add(volume);
            } else {

                //add the previous to the current volume to get our new value
                getValues().add(getValues().get(getValues().size() - 1) + volume);
            }
        }
    }

    private double getMultiplier(Candle candle) {

        //simplify our calculations
        double value1 = candle.getClose() - candle.getLow();
        double value2 = candle.getHigh() - candle.getClose();
        double value3 = candle.getHigh() - candle.getLow();

        //return 0 if either of these are 0
        if (value3 == 0)
            return 0;
        if (value1 - value2 == 0)
            return 0;

        //return our result
        return (value1 - value2) / value3;
    }
}
