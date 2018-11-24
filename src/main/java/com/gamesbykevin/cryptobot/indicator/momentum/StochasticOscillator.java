package com.gamesbykevin.cryptobot.indicator.momentum;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import com.gamesbykevin.cryptobot.indicator.trend.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.List;

public class StochasticOscillator extends Indicator {

    //periods to calculate our sma
    private final int periodsDSma, periodsKSma;

    //our list of sma values
    private List<Double> slowD, slowK;

    public StochasticOscillator(int periodsK, int periodsKSma, int periodsDSma) {

        //call parent
        super(Key.StochasticOscillator, periodsK, Fields.Close);

        //store the periods
        this.periodsDSma = periodsDSma;
        this.periodsKSma = periodsKSma;
    }

    @Override
    public void calculate(List<Candle> candles) throws Exception {

        //clear list(s)
        getFastK().clear();
        getSlowK().clear();
        getSlowD().clear();

        for (int index = 0; index < candles.size(); index++) {

            //make sure we have enough data
            if (index < getPeriods() - 1)
                continue;

            //we need to find the highest high, and the lowest low
            double high = candles.get(index).getHigh();
            double low = candles.get(index).getLow();

            for (int i = index - getPeriods() + 1; i <= index; i++) {

                //identify the highest high
                if (candles.get(i).getHigh() > high)
                    high = candles.get(i).getHigh();

                //identify the lowest low
                if (candles.get(i).getLow() < low)
                    low = candles.get(i).getLow();
            }

            //calculate the oscillator
            double k = (getValue(candles.get(index)) - low) / (high - low);

            //multiply by 100
            k *= 100d;

            //add to the list of values
            getValues().add(k);
        }

        //calculate sma now
        for (int index = 0; index < getValues().size(); index++) {

            //make sure we have enough data
            if (index < periodsKSma - 1)
                continue;

            //add the sma to our list
            getSlowK().add(SimpleMovingAverage.calculate(getValues(), index - periodsKSma + 1, index));
        }

        //now calculate an sma of the smaK
        for (int index = 0; index < getSlowK().size(); index++) {

            //make sure we have enough data
            if (index < periodsDSma - 1)
                continue;

            //add the sma to our list
            getSlowD().add(SimpleMovingAverage.calculate(getSlowK(), index - periodsDSma + 1, index));
        }
    }

    public List<Double> getFastK() {
        return getValues();
    }

    public List<Double> getSlowD() {

        if (this.slowD == null)
            this.slowD = new ArrayList<>();

        return this.slowD;
    }

    public List<Double> getSlowK() {

        if (this.slowK == null)
            this.slowK = new ArrayList<>();

        return this.slowK;
    }

    @Override
    public void display() {
        display("Fast %K (" + getPeriods() + "): ", getFastK());
        display("Slow %K (" + periodsKSma  + "): ", getSlowK());
        display("Slow %D (" + periodsDSma  + "): ", getSlowD());
    }
}