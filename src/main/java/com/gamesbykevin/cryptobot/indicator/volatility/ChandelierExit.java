package com.gamesbykevin.cryptobot.indicator.volatility;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public class ChandelierExit extends Indicator {

    //we need the average true range to calculate our value
    private AverageTrueRange averageTrueRange;

    //our chandelier values
    private List<Double> exitLong, exitShort;

    //identify our highest / lowest values
    private List<Double> highest, lowest;

    public ChandelierExit(int periods) {
        super(Key.ChandelierExit, periods, null);

        //create our indicator dependency
        this.averageTrueRange = new AverageTrueRange(periods);
    }

    @Override
    public void calculate(List<Candle> candles) throws Exception {

        //clear list
        getValues().clear();
        getExitLong().clear();
        getExitShort().clear();
        getHighest().clear();
        getLowest().clear();

        //calculate atr
        getAverageTrueRange().calculate(candles);

        //identify our highest and lowest values
        for (int index = 0; index < candles.size(); index++) {

            //make sure we have enough data
            if (index < getPeriods() - 1)
                continue;

            //add our high / low values
            getHighest().add(identifyValue(candles, index - getPeriods() + 1, index, true));
            getLowest().add(identifyValue(candles, index - getPeriods() + 1, index, false));
        }

        //size of the lists should match
        if (getHighest().size() != getAverageTrueRange().getValues().size())
            throw new Exception("Size does not match getHighest(): " + getHighest().size() + ", getAverageTrueRange(): " + getAverageTrueRange().getValues().size());

        //now we can calculate our long & short exit values
        for (int index = 0; index < getHighest().size(); index++) {

            double atr = (getAverageTrueRange().getValues().get(index) * 3);

            getExitLong().add(getHighest().get(index) - atr);
            getExitShort().add(getLowest().get(index) + atr);
        }
    }

    private List<Double> getHighest() {

        if (this.highest == null)
            this.highest = new ArrayList<>();

        return this.highest;
    }

    private List<Double> getLowest() {

        if (this.lowest == null)
            this.lowest = new ArrayList<>();

        return this.lowest;
    }

    private double identifyValue(List<Candle> candles, int start, int end, boolean highest) {

        //look for the result
        double result = (highest) ? candles.get(start).getHigh() : candles.get(start).getLow();

        for (int index = start; index <= end; index++) {

            //are we looking to the highest or lowest
            if (highest) {

                if (candles.get(index).getHigh() > result)
                    result = candles.get(index).getHigh();

            } else {

                if (candles.get(index).getLow() < result)
                    result = candles.get(index).getLow();

            }
        }

        //return our result
        return result;
    }


    public List<Double> getExitLong() {

        if (this.exitLong == null)
            this.exitLong = new ArrayList<>();

        return exitLong;
    }

    public List<Double> getExitShort() {

        if (this.exitShort == null)
            this.exitShort = new ArrayList<>();

        return exitShort;
    }

    private AverageTrueRange getAverageTrueRange() {
        return this.averageTrueRange;
    }

    @Override
    public void display() {
        display("Exit long : " , getExitLong());
        display("Exit short: ", getExitShort());
    }
}