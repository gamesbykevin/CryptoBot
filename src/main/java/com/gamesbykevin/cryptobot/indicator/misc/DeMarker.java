package com.gamesbykevin.cryptobot.indicator.misc;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import com.gamesbykevin.cryptobot.indicator.trend.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.List;

public class DeMarker extends Indicator {

    private List<Double> deMax, deMin;

    public DeMarker(int periods) {
        super(Key.DeMarker, periods, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear values
        getValues().clear();
        getDeMin().clear();
        getDeMax().clear();

        //identify our min / max values
        for (int index = 1; index < candles.size(); index++) {

            Candle candle = candles.get(index);
            Candle previous = candles.get(index - 1);

            if (candle.getHigh() > previous.getHigh()) {
                getDeMax().add(candle.getHigh() - previous.getHigh());
            } else {
                getDeMax().add(0d);
            }

            if (candle.getLow() < previous.getLow()) {
                getDeMin().add(previous.getLow() - candle.getLow());
            } else {
                getDeMin().add(0d);
            }
        }

        //now we can calculate our final value(s)
        for (int index = 0; index < getDeMax().size(); index++) {

            //we need to ensure we have enough data
            if (index < getPeriods() - 1)
                continue;

            //calculate our numerator / denominator values
            double max = SimpleMovingAverage.calculate(getDeMax(), index - getPeriods() + 1, index);
            double min = SimpleMovingAverage.calculate(getDeMin(), index - getPeriods() + 1, index);
            double demarker = 0;

            //calculate our final value but make sure we don't divide by 0
            if (max + min != 0 && max != 0)
                demarker = (max / (max + min));

            //keep within boundary
            if (demarker > 100)
                demarker = 100;
            if (demarker < 0)
                demarker = 0;

            //add the final result to our list
            getValues().add(demarker);
        }
    }

    private List<Double> getDeMax() {

        if (this.deMax == null)
            this.deMax = new ArrayList<>();

        return this.deMax;
    }

    private List<Double> getDeMin() {

        if (this.deMin == null)
            this.deMin = new ArrayList<>();

        return this.deMin;
    }

    @Override
    public void display() {
        displayDefault();
    }
}