package com.gamesbykevin.cryptobot.indicator.momentum;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import com.gamesbykevin.cryptobot.indicator.trend.ExponentialMovingAverage;
import com.gamesbykevin.cryptobot.indicator.trend.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.List;

public class TrueStrengthIndex extends Indicator {

    //track the price change
    private List<Double> priceChange, priceChangeAbsolute;

    //track the short and long ema
    private List<Double> longEma, longEmaAbsolute, shortEma, shortEmaAbsolute;

    //signal line of tsi
    private List<Double> signalLine;

    //track short ema periods
    private final int periodsShort;

    //how many periods to create our signal line
    private final int periodsSignal;

    public TrueStrengthIndex(int periods, int periodsShort, int periodsSignal) {
        this(periods, periodsShort, periodsSignal, Fields.Close);
    }

    public TrueStrengthIndex(int periods, int periodsShort, int periodsSignal, Fields field) {

        //call parent
        super(Key.TrueStrengthIndex, periods, field);

        //track the periods
        this.periodsShort = periodsShort;
        this.periodsSignal = periodsSignal;
    }

    @Override
    public void calculate(List<Candle> candles) throws Exception {

        //clear values
        getValues().clear();
        getPriceChange().clear();
        getLongEma().clear();
        getShortEma().clear();
        getPriceChangeAbsolute().clear();
        getLongEmaAbsolute().clear();
        getShortEmaAbsolute().clear();
        getSignalLine().clear();

        //calculate the price change
        for (int index = 1; index < candles.size(); index++) {

            //calculate the difference from one period to the next
            double difference = getValue(candles.get(index)) - getValue(candles.get(index - 1));

            //add the price change to our list
            getPriceChange().add(difference);
            getPriceChangeAbsolute().add(Math.abs(difference));
        }

        //calculate the ema values for regular and absolute
        calculateEmas(getPriceChange(), getLongEma(), getShortEma());
        calculateEmas(getPriceChangeAbsolute(), getLongEmaAbsolute(), getShortEmaAbsolute());

        //calculate our tsi value
        for (int index = 0; index < getShortEma().size(); index++) {
            getValues().add((getShortEma().get(index) / getShortEmaAbsolute().get(index)) * 100.0d);
        }

        //first value is the sma
        double sma = SimpleMovingAverage.calculate(getValues(), 0, this.periodsSignal - 1);
        getSignalLine().add(sma);

        //create our signal line
        for (int index = this.periodsSignal; index < getValues().size(); index++) {
            getSignalLine().add(ExponentialMovingAverage.calculate(getValues().get(index), getPeriods(), getSignalLine().get(getSignalLine().size() - 1)));
        }
    }

    private void calculateEmas(List<Double> priceChangeList, List<Double> longEmaList, List<Double> shortEmaList) {

        //first value is the sma
        double sma = SimpleMovingAverage.calculate(priceChangeList, 0, getPeriods() - 1);

        //add our first value
        longEmaList.add(sma);

        //calculate the rest of our values
        for (int index = getPeriods(); index < priceChangeList.size(); index++) {

            //calculate the exponential moving average
            double ema = ExponentialMovingAverage.calculate(priceChangeList.get(index), getPeriods(), longEmaList.get(longEmaList.size() - 1));

            //add it to our list
            longEmaList.add(ema);
        }

        //first value is the sma
        sma = SimpleMovingAverage.calculate(longEmaList, 0, this.periodsShort - 1);

        //add our first value
        shortEmaList.add(sma);

        //calculate the rest of our values
        for (int index = this.periodsShort; index < longEmaList.size(); index++) {

            //calculate the exponential moving average
            double ema = ExponentialMovingAverage.calculate(longEmaList.get(index), getPeriods(), shortEmaList.get(shortEmaList.size() - 1));

            //add it to our list
            shortEmaList.add(ema);
        }
    }

    public List<Double> getSignalLine() {

        if (this.signalLine == null)
            this.signalLine = new ArrayList<>();

        return this.signalLine;
    }

    private List<Double> getPriceChange() {

        if (this.priceChange == null)
            this.priceChange = new ArrayList<>();

        return this.priceChange;
    }

    private List<Double> getPriceChangeAbsolute() {

        if (this.priceChangeAbsolute == null)
            this.priceChangeAbsolute = new ArrayList<>();

        return this.priceChangeAbsolute;
    }

    private List<Double> getLongEma() {

        if (this.longEma == null)
            this.longEma = new ArrayList<>();

        return this.longEma;
    }

    private List<Double> getLongEmaAbsolute() {

        if (this.longEmaAbsolute == null)
            this.longEmaAbsolute = new ArrayList<>();

        return this.longEmaAbsolute;
    }

    private List<Double> getShortEma() {

        if (this.shortEma == null)
            this.shortEma = new ArrayList<>();

        return this.shortEma;
    }

    private List<Double> getShortEmaAbsolute() {

        if (this.shortEmaAbsolute == null)
            this.shortEmaAbsolute = new ArrayList<>();

        return this.shortEmaAbsolute;
    }

    @Override
    public void display() {
        display("Double smoothed: ", getShortEma());
        display("Double smoothed abs: ", getShortEmaAbsolute());
        displayDefault();
        display("Signal Line (" + this.periodsSignal + "): ", getSignalLine());
    }
}