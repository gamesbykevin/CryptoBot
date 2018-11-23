package com.gamesbykevin.cryptobot.indicator.volatility;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import com.gamesbykevin.cryptobot.indicator.trend.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.List;

public class BollingerBands extends Indicator {

    //the upper and lower
    private List<Double> upper, lower;

    //our chosen multiplier for standard deviation
    private final float multiplier;

    /**
     * What do we multiply the standard deviation by when calculating the bollinger bands
     */
    public static final float STANDARD_DEVIATION_MULTIPLIER_DEFAULT = 2.0f;

    public BollingerBands(int periods) {
        this(periods, STANDARD_DEVIATION_MULTIPLIER_DEFAULT, Fields.Close);
    }

    public BollingerBands(int periods, float multiplier, Fields field) {

        //call parent
        super(Key.BollingerBands, periods, field);

        //assign our multiplier
        this.multiplier = multiplier;
    }

    @Override
    public void calculate(List<Candle> candles) throws Exception {

        //clear our list
        getValues().clear();
        getUpper().clear();
        getLower().clear();

        //determine the upper/lower bands
        for (int index = 0; index < candles.size(); index++) {

            //make sure we have enough data
            if (index < getPeriods() - 1)
                continue;

            final int start = index - getPeriods() + 1;
            final int end = index;

            //calculate sma
            final double sma = SimpleMovingAverage.calculate(candles, start, end, getField());

            double deviationSum = 0;

            //calculate the deviation to get the standard deviation
            for (int i = start; i <= end; i++) {

                //subtract the closing price from the average to get the deviation
                double deviation = getValue(candles.get(i)) - sma;

                //square the deviation
                double squared = Math.pow(deviation, 2);

                //add it to the total
                deviationSum += squared;
            }

            //calculate standard deviation
            double standardDeviation = (Math.sqrt(deviationSum / getPeriods()) * multiplier);

            //add the middle lime
            getMiddle().add(sma);

            //add the upper and lower lines
            getUpper().add(sma + standardDeviation);
            getLower().add(sma - standardDeviation);
        }
    }

    public List<Double> getMiddle() {
        return getValues();
    }

    public List<Double> getUpper() {

        if (this.upper == null)
            this.upper = new ArrayList<>();

        return this.upper;
    }

    public List<Double> getLower() {

        if (this.lower == null)
            this.lower = new ArrayList<>();

        return this.lower;
    }

    @Override
    public void display() {
        display("Upper (" + getPeriods() + "): ", getUpper());
        display("Middle(" + getPeriods() + "): ", getMiddle());
        display("Lower (" + getPeriods() + "): ", getLower());
    }
}