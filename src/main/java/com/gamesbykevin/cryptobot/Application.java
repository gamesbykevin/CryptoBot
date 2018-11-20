package com.gamesbykevin.cryptobot;

import com.gamesbykevin.cryptobot.broker.BrokerManager;
import com.gamesbykevin.cryptobot.util.Properties;

public class Application {

    public static void main(String[] args) {
        getBrokerManager().start();
    }

    private static BrokerManager MANAGER;

    public static BrokerManager getBrokerManager() {

        if (MANAGER == null) {

            //create new broker manager
            MANAGER = new BrokerManager();

            //display properties loaded
            Properties.display();
        }

        return MANAGER;
    }
}