package com.gamesbykevin.cryptobot.indicator.volume;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.List;

public class PositiveVolumeIndex extends Indicator {

    public PositiveVolumeIndex() {
        super(Key.PositiveVolumeIndex, 0, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear list
        getValues().clear();

        for (int index = 1; index < candles.size(); index++) {

            //get the previous value
            double previous = (getValues().isEmpty()) ? 1d : getValues().get(getValues().size() - 1);

            if (candles.get(index).getVolume() > candles.get(index - 1).getVolume()) {

                //calculate closing price percent change
                double value = (candles.get(index).getClose() - candles.get(index - 1).getClose()) / candles.get(index - 1).getClose();

                //add to our list
                getValues().add(previous + (value * previous));

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