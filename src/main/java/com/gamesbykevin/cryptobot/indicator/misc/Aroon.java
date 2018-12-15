package com.gamesbykevin.cryptobot.indicator.misc;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public class Aroon extends Indicator {

    private List<Double> aroonUp, aroonDown;

    public Aroon(int periods) {
        super(Key.Aroon, periods, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear values
        getValues().clear();
        getAroonDown().clear();
        getAroonUp().clear();

        //identify the last high and low
        for (int index = 0; index < candles.size(); index++) {

            //wait till we have enough data to calculate
            if (index < getPeriods() - 1)
                continue;

            //calculate our new values
            double aroonU = ((getPeriods() - getDays(candles, index, true))  / getPeriods()) * 100;
            double aroonD = ((getPeriods() - getDays(candles, index, false)) / getPeriods()) * 100;

            //values have a range
            if (aroonU < 0)
                aroonU = 0;
            if (aroonD < 0)
                aroonD = 0;
            if (aroonU > 100)
                aroonU = 100;
            if (aroonD > 100)
                aroonD = 100;

            //add them to the lists
            getAroonUp().add(aroonU);
            getAroonDown().add(aroonD);
        }
    }

    private int getDays(List<Candle> candles, int start, boolean high) {

        //let's set the bar here
        double value = (high) ? candles.get(start).getHigh() : candles.get(start).getLow();

        //where did we find the last high / low
        int idx = start;

        //go backwards to locate the latest high / low
        for (int index = start; index > start - getPeriods(); index--) {

            //are we looking for the last high or the last low
            if (high) {

                //if we have a new high track the winner
                if (candles.get(index).getHigh() > value) {
                    value = candles.get(index).getHigh();
                    idx = index;
                }

            } else {

                //if we have a new low track the winner
                if (candles.get(index).getLow() < value) {
                    value = candles.get(index).getLow();
                    idx = index;
                }

            }
        }

        //return the number of days since the winner
        return (start - idx);
    }

    public List<Double> getAroonDown() {

        if (this.aroonDown == null)
            this.aroonDown = new ArrayList<>();

        return this.aroonDown;
    }

    public List<Double> getAroonUp() {

        if (this.aroonUp == null)
            this.aroonUp = new ArrayList<>();

        return this.aroonUp;
    }

    @Override
    public void display() {
        display("Aroon Up  : ", getAroonUp());
        display("Aroon Down: ", getAroonDown());
    }
}