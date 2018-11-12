package com.gamesbykevin.cryptobot.indicator.trend;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.candle.Candle.Fields;
import com.gamesbykevin.cryptobot.indicator.Indicator;
import lombok.Data;

import java.util.List;

@Data
public class SimpleMovingAverage extends Indicator {

    public SimpleMovingAverage(int periods, Fields field) {
        super(Key.SimpleMovingAverage, periods, field);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear our list
        getValues().clear();

        //look at the history in order to calculate our values
        for (int index = 0; index <= candles.size(); index++) {

            //make sure we have enough data first
            if (index < getPeriods())
                continue;

            double sum = 0;

            //check the periods to perform our calculation
            for (int j = index - getPeriods(); j < index; j++) {

                //which field?
                switch (getField()) {

                    case Open:
                        sum += candles.get(j).getOpen();
                        break;

                    case Close:
                        sum += candles.get(j).getClose();
                        break;

                    case Low:
                        sum += candles.get(j).getLow();
                        break;

                    case High:
                        sum += candles.get(j).getHigh();
                        break;

                    case Volume:
                        sum += candles.get(j).getVolume();
                        break;
                }
            }

            //add the value to our list
            getValues().add((sum / (double)getPeriods()));
        }
    }

}