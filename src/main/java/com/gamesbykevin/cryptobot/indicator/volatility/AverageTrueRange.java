package com.gamesbykevin.cryptobot.indicator.volatility;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public class AverageTrueRange extends Indicator {

    //list of true range values
    private List<Double> trueRange;

    public AverageTrueRange(int periods) {
        super(Key.AverageTrueRange, periods, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear values
        getValues().clear();
        getTrueRange().clear();

        for (int index = 1; index < candles.size(); index++) {

            //get current and previous candles
            Candle candle = candles.get(index);
            Candle previous = candles.get(index - 1);

            //the greatest of the 3 below will be the true range
            double method1 = candle.getHigh() - candle.getLow();
            double method2 = Math.abs(candle.getHigh() - previous.getClose());
            double method3 = Math.abs(candle.getLow() - previous.getLow());

            //what is our true range
            double trueRange = -1;

            //the greatest value will be our true range
            if (method1 > trueRange)
                trueRange = method1;
            if (method2 > trueRange)
                trueRange = method2;
            if (method3 > trueRange)
                trueRange = method3;

            //add the true range to our list
            getTrueRange().add(trueRange);
        }

        //now we calculate the average true range
        for (int index = 0; index < getTrueRange().size(); index++) {

            //skip until we have enough data to calculate
            if (index < getPeriods() - 1)
                continue;

            double atr = 0;

            if (getValues().isEmpty()) {

                //first value will be an average
                atr = getAverage(getTrueRange(), index - getPeriods() + 1, index);

            } else {

                //get the previous value
                double atrPrevious = getValues().get(getValues().size() - 1);

                //calculate the new atr value
                atr = ((atrPrevious * (getPeriods() - 1)) + getTrueRange().get(index)) / getPeriods();
            }

            //add the average to our list
            getValues().add(atr);
        }
    }

    private double getAverage(List<Double> list, int start, int end) {

        double sum = 0;

        //calculate the sum
        for (int index = start; index <= end; index++) {
            sum += list.get(index);
        }

        //return our result which is the average
        return (sum / (end - start + 1));
    }

    private List<Double> getTrueRange() {

        if (this.trueRange == null)
            this.trueRange = new ArrayList<>();

        return this.trueRange;
    }

    @Override
    public void display() {
        displayDefault();
    }
}