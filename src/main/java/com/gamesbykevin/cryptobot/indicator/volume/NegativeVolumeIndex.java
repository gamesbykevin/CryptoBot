package com.gamesbykevin.cryptobot.indicator.volume;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.List;

public class NegativeVolumeIndex extends Indicator {

    public NegativeVolumeIndex() {
        super(Key.NegativeVolumeIndex, 0, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear list
        getValues().clear();

        for (int index = 1; index < candles.size(); index++) {

            //get the previous value
            double previous = (getValues().isEmpty()) ? 1000d : getValues().get(getValues().size() - 1);

            //if the volume decreased
            if (candles.get(index).getVolume() < candles.get(index - 1).getVolume()) {

                //calculate closing price percent change
                double value = (candles.get(index).getClose() - candles.get(index - 1).getClose()) / candles.get(index - 1).getClose();

                //add to our list
                getValues().add(previous + value);

            } else {

                //value remains unchanged
                getValues().add(previous);
            }
        }
    }

    @Override
    public void display() {
        displayDefault();
    }
}