package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Strategy {

    //list of indicators this strategy is using
    private List<Indicator> indicators;

    /**
     * Each strategy will have it's own key
     */
    public enum Key {

        Strategy_1,
        Strategy_2,
        Strategy_3,
        Strategy_4,
        Strategy_5,
        Strategy_6,
        Strategy_7
    }

    //which strategy are we using
    private final Key key;

    /**
     * Add indicator to our list
     * @param indicator Desired indicator to add
     * @return index position so we can access the indicator
     */
    protected int addIndicator(Indicator indicator) {

        //instantiate if null
        if (getIndicators() == null)
            this.indicators = new ArrayList<>();

        //add the indicator to the list
        getIndicators().add(indicator);

        //the latest index will be for this indicator
        return (getIndicators().size() - 1);
    }

    protected Indicator getIndicator(int index) {
        return getIndicators().get(index);
    }

    public void calculate(List<Candle> candles) throws Exception {

        //perform calculations for each indicator
        for (int index = 0; index < getIndicators().size(); index++) {
            getIndicator(index).calculate(candles);
        }

        //display our data
        display();
    }

    /**
     * Logic to determine if we have a buy signal
     * @return true if buy signal, false otherwise
     */
    public abstract boolean hasSignalBuy();

    /**
     * Logic to determine if we have a sell signal
     * @return true if sell signal, false otherwise
     */
    public abstract boolean hasSignalSell();

    public void display() {

        for (int index = 0; index < getIndicators().size(); index++) {
            getIndicator(index).display();
        }
    }

    protected double getRecent(List<Double> values) {
        return getRecent(values, 1);
    }

    protected double getRecent(List<Double> values, int index) {
        return values.get(values.size() - index);
    }
}