package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.strategy.Strategy.Key;

public class StrategyHelper {

    public static Strategy create(Key key) throws Exception {

        Strategy strategy = null;

        //create the appropriate strategy
        switch (key) {

            case Strategy_1:
                strategy = new Strategy1();
                break;

            default:
                throw new Exception();
        }

        //return our strategy
        return strategy;
    }
}