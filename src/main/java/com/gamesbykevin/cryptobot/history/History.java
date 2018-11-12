package com.gamesbykevin.cryptobot.history;

import com.gamesbykevin.cryptobot.candle.Candle;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class History {

    //our list of historical candle data
    private List<Candle> candles;

    //the most recent candle timestamp
    private long recent = 0;

    public History() {
        this.candles = new ArrayList<>();
    }

    private void setRecent(long recent) {
        this.recent = recent;
    }

    public void add(double open, double close, double high, double low, double volume, long time) {

        Candle match = null;

        for (int index = getCandles().size() - 1; index >= 0; index--) {

            //if the time matches, then we will update this candle
            if (getCandles().get(index).getTime() == time) {
                match = getCandles().get(index);
                break;
            }
        }

        //if we found a matching candle update it
        if (match != null) {

            //update the existing candle
            match.setOpen(open);
            match.setClose(close);
            match.setLow(low);
            match.setHigh(high);
            match.setVolume(volume);

        } else {

            //create our candle
            Candle candle = new Candle();
            candle.setOpen(open);
            candle.setClose(close);
            candle.setLow(low);
            candle.setHigh(high);
            candle.setVolume(volume);
            candle.setTime(time);

            //since we didn't find it, let's add it
            getCandles().add(candle);

        }

        //keep track of the most recent time
        if (time > getRecent())
            setRecent(time);

        //sort the data
        sort();
    }

    /**
     * Sort the candles based on their time in ascending order
     */
    public void sort() {

        for (int i = 0; i < getCandles().size(); i++) {

            for (int j = i + 1; j < getCandles().size(); j++) {

                Candle candle1 = getCandles().get(i);
                Candle candle2 = getCandles().get(j);

                //we want the most recent candles on the end
                if (candle1.getTime() > candle2.getTime()) {

                    //swap values
                    getCandles().set(i, candle2);
                    getCandles().set(j, candle1);
                }
            }
        }
    }
}