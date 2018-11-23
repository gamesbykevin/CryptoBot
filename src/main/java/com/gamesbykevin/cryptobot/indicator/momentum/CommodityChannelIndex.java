package com.gamesbykevin.cryptobot.indicator.momentum;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import com.gamesbykevin.cryptobot.indicator.trend.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.List;

public class CommodityChannelIndex extends Indicator {

    /**
     * Used to ensure a majority of values are between -100 and 100
     */
    private static final float CONSTANT_VALUE = .015f;

    //list of typical prices
    private List<Double> typicalPrice;

    public CommodityChannelIndex(int periods) {
        super(Key.CommodityChannelIndex, periods, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear our list of values
        getTypicalPrice().clear();
        getValues().clear();

        //calculate typical price
        for (int index = 0; index < candles.size(); index++) {
            getTypicalPrice().add(calculateTypicalPrice(candles.get(index)));
        }

        //calculate our final values
        for (int index = 0; index < getTypicalPrice().size(); index++) {

            if (index < getPeriods() - 1)
                continue;

            //where do we start and end
            int start = index - getPeriods() + 1;
            int end = index;

            //calculate the sma
            double sma = SimpleMovingAverage.calculate(getTypicalPrice(), start, end);

            //needed for mean deviation
            double sum = 0;

            for (int i = start; i <= end; i++) {
                sum += Math.abs(getTypicalPrice().get(i) - sma);
            }

            //calculate our deviation
            double deviation = (sum / getPeriods());

            //calculate our commodity channel index
            double cci = (getTypicalPrice().get(index) - sma) / (CONSTANT_VALUE * deviation);

            //add to our list
            getValues().add(cci);
        }
    }

    private double calculateTypicalPrice(Candle candle) {
        return ((candle.getHigh() + candle.getLow() + candle.getClose()) / 3.0d);
    }

    private List<Double> getTypicalPrice() {

        if (this.typicalPrice == null)
            this.typicalPrice = new ArrayList<>();

        return this.typicalPrice;
    }

    @Override
    public void display() {
        displayDefault();
    }
}