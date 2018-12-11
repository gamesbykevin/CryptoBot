package com.gamesbykevin.cryptobot.indicator.trend;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public class Envelopes extends Indicator {

    //simple moving average
    private final SimpleMovingAverage sma;

    //the upper and lower
    private List<Double> upper, lower;

    //how much do we offset the upper/lower
    public static final float RATIO = 0.05f;

    private final float ratio;

    public Envelopes(int periods) {
        this(periods, Fields.Close);
    }

    public Envelopes(int periods, Fields field) {
        this(periods, field, RATIO);
    }

    public Envelopes(int periods, Fields field, float ratio) {

        //call parent constructor
        super(Key.Envelopes, periods, field);

        //our chosen ratio
        this.ratio = ratio;

        //create new instances
        this.sma = new SimpleMovingAverage(periods, field);
        this.upper = new ArrayList<>();
        this.lower = new ArrayList<>();
    }

    @Override
    public void calculate(List<Candle> candles) throws Exception {

        //calculate our average
        getSma().calculate(candles);
        getUpper().clear();
        getLower().clear();

        for (int index = 0; index < getSma().getValues().size(); index++) {

            double sma = getSma().getValues().get(index);

            //add our upper and lower
            getUpper().add(sma + (sma * getRatio()));
            getLower().add(sma - (sma * getRatio()));
        }
    }

    @Override
    public void display() {
        display("Upper: ", getUpper());
        getSma().display();
        display("Lower: ", getLower());
    }

    public SimpleMovingAverage getSma() {
        return this.sma;
    }

    public List<Double> getUpper() {
        return this.upper;
    }

    public List<Double> getLower() {
        return this.lower;
    }

    public float getRatio() {
        return this.ratio;
    }
}