package com.gamesbykevin.cryptobot.indicator;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Indicator {

    /**
     * Each indicator will have it's own key
     */
    public enum Key {

        //trend
        SimpleMovingAverage,
    }

    //the indicator we are using
    private final Key key;

    //each indicator will be calculating for a certain number of periods
    private final int periods;

    //what field are we calculating?
    private final Fields field;

    //list of values accessible to our indicator
    private List<Double> values;

    //implement logic to calculate
    public abstract void calculate(List<Candle> candles);

    public List<Double> getValues() {

        if (this.values == null)
            this.values = new ArrayList<>();

        return this.values;
    }

    public void display(String desc) {

        String tmp = "";

        for (int index = getValues().size() - 1; index >= 0; index--) {

            if (tmp != null && tmp.trim().length() > 0)
                tmp += ", ";

            tmp += getValues().get(index);
        }

        System.out.println(desc + " " + tmp);
    }
}