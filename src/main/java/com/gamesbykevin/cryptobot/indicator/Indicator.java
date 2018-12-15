package com.gamesbykevin.cryptobot.indicator;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

@Data
@Log4j
public abstract class Indicator {

    /**
     * Each indicator will have it's own key
     */
    public enum Key {

        //misc
        IchimokuCloud,

        //trend
        Envelopes,
        ExponentialMovingAverage,
        MovingAverageConvergenceDivergence,
        SimpleMovingAverage,

        //momentum
        CommodityChannelIndex,
        OnBalanceVolume,
        RelativeStrengthIndex,
        StochasticOscillator,
        TrueStrengthIndex,

        //volatility
        BollingerBands,

        //volume
        AccumulationDistributionLine,
        MoneyFlowIndex,
        NegativeVolumeIndex,
        PositiveVolumeIndex
    }

    /**
     * How many indicator values do we print?
     */
    public static final int DISPLAY_LIMIT = 5;

    //the indicator we are using
    private final Key key;

    //each indicator will be calculating for a certain number of periods
    private final int periods;

    //what field are we calculating?
    private final Fields field;

    //list of values accessible to our indicator
    private List<Double> values;

    //implement logic to calculate
    public abstract void calculate(List<Candle> candles) throws Exception;

    public List<Double> getValues() {

        if (this.values == null)
            this.values = new ArrayList<>();

        return this.values;
    }

    protected void displayDefault() {
        display(getKey() + " (" + getPeriods() + "): ", getValues());
    }

    protected void display(String desc, List<Double> values) {

        try {

            String tmp = "";

            //display the most recent values
            for (int index = values.size() - DISPLAY_LIMIT; index < values.size(); index++) {

                //each value is separated by a comma
                if (tmp != null && tmp.trim().length() > 0)
                    tmp += ", ";

                tmp += values.get(index);
            }

            log.info(desc + tmp);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Create logic to print indicator data
     */
    public abstract void display();

    protected double getValue(Candle candle) throws Exception {
        return getValue(candle, getField());
    }

    protected static double getValue(Candle candle, Fields field) throws Exception {

        //which field?
        switch (field) {

            case Open:
                return candle.getOpen();

            case Close:
                return candle.getClose();

            case Low:
                return candle.getLow();

            case High:
                return candle.getHigh();

            case Volume:
                return candle.getVolume();
        }

        throw new Exception("Field not mapped: " + field);
    }
}