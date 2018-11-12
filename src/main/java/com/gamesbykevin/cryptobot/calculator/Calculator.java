package com.gamesbykevin.cryptobot.calculator;

import com.gamesbykevin.cryptobot.broker.Broker;
import com.gamesbykevin.cryptobot.history.History;
import com.gamesbykevin.cryptobot.strategy.Strategy;
import com.gamesbykevin.cryptobot.strategy.Strategy.Key;
import com.gamesbykevin.cryptobot.strategy.StrategyHelper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Calculator {

    //list of strategies we are calculating
    private List<Strategy> strategies;

    //our historical data
    private History history;

    public Calculator() throws Exception {

        this.history = new History();
        this.strategies = new ArrayList<>();

        for (Key key : Key.values()) {
            getStrategies().add(StrategyHelper.create(key));
        }
    }

    public History getHistory() {
        return this.history;
    }

    protected List<Strategy> getStrategies() {
        return this.strategies;
    }

    protected Strategy getStrategy(int index) {
        return getStrategies().get(index);
    }

    public abstract void update(Broker broker);
}