package com.gamesbykevin.cryptobot.indicator.momentum;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.List;

public class MoneyFlowIndex extends Indicator {

    public MoneyFlowIndex(int periods) {
        super(Key.MoneyFlowIndex, periods, null);
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear the list
        getValues().clear();

        for (int index = 0; index < candles.size(); index++) {

            //make sure we have enough data first
            if (index < getPeriods())
                continue;

            //sum our gains and losses
            double moneyFlowPositive = 0;
            double moneyFlowNegative = 0;

            //check the specified range of periods
            for (int i = index - getPeriods() + 1; i <= index; i++) {

                //calculate typical price
                double typicalPrice = ((candles.get(i).getHigh() + candles.get(i).getLow() + candles.get(i).getClose()) / 3);

                //calculate raw money flow
                double rawMoneyFlow = typicalPrice * candles.get(i).getVolume();

                if (candles.get(i).getClose() > candles.get(i - 1).getClose()) {
                    moneyFlowPositive += rawMoneyFlow;
                } else if (candles.get(i).getClose() < candles.get(i - 1).getClose()) {
                    moneyFlowNegative += rawMoneyFlow;
                }
            }

            //calculate our ratio
            double moneyFlowRatio = 0;

            //make sure we don't divide by 0
            if (moneyFlowPositive != 0 && moneyFlowNegative != 0)
                moneyFlowRatio = (moneyFlowPositive / moneyFlowNegative);

            //calculate the money flow index
            double moneyFlowIndex = 100 - (100 / (1 + moneyFlowRatio));

            //add the money flow index to our list
            getValues().add(moneyFlowIndex);
        }
    }
}