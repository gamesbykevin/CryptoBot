package com.gamesbykevin.cryptobot.strategy;

import com.gamesbykevin.cryptobot.strategy.Strategy.Key;

public class StrategyHelper {

    public static Strategy create(String strategyName) throws Exception {

        //look through our list to find the strategy
        for (Key key : Key.values()) {

            //if the string matches create this strategy
            if (key.toString().equalsIgnoreCase(strategyName.trim()))
                return create(key);
        }

        //if we didn't find a match return null
        return null;
    }

    public static Strategy create(Key key) throws Exception {

        Strategy strategy = null;

        //create the appropriate strategy
        switch (key) {

            case Strategy_1:
                strategy = new Strategy1();
                break;

            case Strategy_2:
                strategy = new Strategy2();
                break;

            case Strategy_3:
                strategy = new Strategy3();
                break;

            case Strategy_4:
                strategy = new Strategy4();
                break;

            case Strategy_5:
                strategy = new Strategy5();
                break;

            case Strategy_6:
                strategy = new Strategy6();
                break;

            case Strategy_7:
                strategy = new Strategy7();
                break;

            case Strategy_8:
                strategy = new Strategy8();
                break;

            default:
                throw new Exception("Strategy not mapped: " + key);
        }

        //return our strategy
        return strategy;
    }
}