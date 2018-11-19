package com.gamesbykevin.cryptobot.indicator.momentum;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.List;

public class OnBalanceVolume extends Indicator {

    public OnBalanceVolume() {
        super(Key.OnBalanceVolume, 0, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear all existing values
        getValues().clear();

        //check our candles to calculate
        for (int index = 1; index < candles.size(); index++) {

            double volume = 0;

            //we will add the + - for the volume depending on our closing $
            if (candles.get(index).getClose() > candles.get(index - 1).getClose()) {
                volume = candles.get(index).getVolume();
            } else if (candles.get(index).getClose() < candles.get(index - 1).getClose()) {
                volume = -candles.get(index).getVolume();
            }

            if (getValues().isEmpty()) {
                getValues().add(volume);
            } else {

                //get the previous obv
                double prev = getValues().get(getValues().size() - 1);

                //add the cumulative to the list
                getValues().add(prev + volume);
            }
        }
    }
}