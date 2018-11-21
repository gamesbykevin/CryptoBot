package com.gamesbykevin.cryptobot.indicator.trend;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public class MovingAverageConvergenceDivergence extends Indicator {

    //our short and long ema's
    private final ExponentialMovingAverage shortEma;
    private final ExponentialMovingAverage longEma;

    //the signal line
    private List<Double> signal;

    public MovingAverageConvergenceDivergence(int periodsEmaShort, int periodsEmaLong, int periodsSignalLine) throws Exception {

        //call parent
        super(Key.MovingAverageConvergenceDivergence, periodsSignalLine, null);

        //short periods have to be less than the long periods
        if (periodsEmaShort > periodsEmaLong)
            throw new Exception("\"Short periods\" should be less than the \"Long periods\"");

        //create our ema indicators
        this.shortEma = new ExponentialMovingAverage(periodsEmaShort, Fields.Close);
        this.longEma = new ExponentialMovingAverage(periodsEmaLong, Fields.Close);
    }

    @Override
    public void calculate(List<Candle> candles) throws Exception {

        //clear our list
        getValues().clear();

        //calculate our ema values
        getShortEma().calculate(candles);
        getLongEma().calculate(candles);

        //clear the signal line
        getSignalLine().clear();

        //period difference so we can compare values accordingly
        int difference = getShortEma().getValues().size() - getLongEma().getValues().size();

        for (int index = 0; index < getLongEma().getValues().size(); index++) {

            //subtract long from short for our histogram
            getValues().add(getShortEma().getValues().get(index + difference) - getLongEma().getValues().get(index));
        }

        //first value in our signal line is the sma
        getSignalLine().add(SimpleMovingAverage.calculate(getValues(), 0, getPeriods() - 1));

        //calculate ema for the remaining values which will be our signal line
        for (int index = 1; index < getValues().size(); index++) {

            //get the previous ema
            double previousEma = getSignalLine().get(getSignalLine().size() - 1);

            //add new ema to the signal line
            getSignalLine().add(ExponentialMovingAverage.calculate(getValues().get(index), getPeriods(), previousEma));
        }
    }

    private ExponentialMovingAverage getLongEma() {
        return this.longEma;
    }

    private ExponentialMovingAverage getShortEma() {
        return this.shortEma;
    }

    public List<Double> getSignalLine() {

        if (this.signal == null)
            this.signal = new ArrayList<>();

        return this.signal;
    }
}
