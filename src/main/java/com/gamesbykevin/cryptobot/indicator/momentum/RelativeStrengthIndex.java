package com.gamesbykevin.cryptobot.indicator.momentum;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public class RelativeStrengthIndex extends Indicator {

    //keep track of our average gains / losses
    private List<Double> avgGain, avgLoss;

    public RelativeStrengthIndex(int periods) {
        this(periods, Fields.Close);
    }

    public RelativeStrengthIndex(int periods, Fields field) {
        super(Key.RelativeStrengthIndex, periods, field);
    }

    @Override
    public void calculate(List<Candle> candles) throws Exception {

        //clear list
        getValues().clear();
        getAvgGain().clear();
        getAvgLoss().clear();

        for (int index = 0; index < candles.size(); index++) {

            //make sure we have enough data
            if (index < getPeriods())
                continue;

            //what is our average gain / loss
            double avgGain = 0, avgLoss = 0;

            //first value is calculated differently
            if (getValues().isEmpty()) {

                double gains = 0;
                double losses = 0;

                for (int j = index - getPeriods() + 1; j <= index; j++) {

                    if (getValue(candles.get(j)) > getValue(candles.get(j - 1))) {

                        gains += (getValue(candles.get(j)) - getValue(candles.get(j - 1)));

                    } else if (getValue(candles.get(j)) < getValue(candles.get(j - 1))) {

                        losses += (getValue(candles.get(j - 1)) - getValue(candles.get(j)));
                    }
                }

                //calculate average
                avgGain = (gains / getPeriods());
                avgLoss = (losses / getPeriods());

            } else {

                //what is the difference from the previous candle
                double difference = Math.abs(getValue(candles.get(index)) - getValue(candles.get(index - 1)));

                //determine the current gain / loss
                double currentGain = getValue(candles.get(index)) > getValue(candles.get(index - 1)) ? difference : 0;
                double currentLoss = getValue(candles.get(index)) < getValue(candles.get(index - 1)) ? difference : 0;

                double previousGain = getAvgGain().get(getAvgGain().size() - 1);
                double previousLoss = getAvgLoss().get(getAvgLoss().size() - 1);

                avgGain = ((previousGain * (getPeriods() - 1)) + currentGain) / getPeriods();
                avgLoss = ((previousLoss * (getPeriods() - 1)) + currentLoss) / getPeriods();
            }

            //add to our list
            getAvgGain().add(avgGain);
            getAvgLoss().add(avgLoss);

            //calculate the relative strength
            double relativeStrength = (avgGain / avgLoss);

            //finally add our relative strength index
            if (avgGain <= 0) {

                //if the gain is 0 our rsi will be 0
                getValues().add(0.0d);

            } else if (avgLoss <= 0) {

                //if the loss is 0 our rsi will be 100
                getValues().add(100.0d);

            } else {

                //else calculate the relative strength index
                getValues().add(100.0f - (100.0f / (1.0f + relativeStrength)));
            }
        }
    }

    private List<Double> getAvgGain() {

        if (this.avgGain == null)
            this.avgGain = new ArrayList<>();

        return this.avgGain;
    }

    private List<Double> getAvgLoss() {

        if (this.avgLoss == null)
            this.avgLoss = new ArrayList<>();

        return this.avgLoss;
    }

    @Override
    public void display() {
        displayDefault();
    }
}