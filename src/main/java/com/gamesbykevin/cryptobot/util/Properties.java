package com.gamesbykevin.cryptobot.util;

import java.io.FileInputStream;

import static com.gamesbykevin.cryptobot.util.Util.display;

public class Properties {

    //object used to access our properties
    private static java.util.Properties PROPERTIES;

    //location of property file
    public static final String PROPERTY_FILE = "./config.properties";

    /**
     * Are we paper trading? (aka not using real money)
     */
    public static final boolean PAPER_TRADING = Boolean.parseBoolean(getProperty("paperTrading"));

    public static final String[] STRATEGIES = getProperty("strategies").split(",");
    public static final String[] DATA_FEED_URL = getProperty("dataFeedUrl").split(",");
    public static final String[] TICKER_PRICE_URL = getProperty("tickerPriceUrl").split(",");

    public static String getProperty(String key) {

        try {

            //if null, instantiate and load
            if (PROPERTIES == null) {

                //loading...
                display("Loading properties: " + PROPERTY_FILE);

                //create our object
                PROPERTIES = new java.util.Properties();

                try {

                    //load file from same directory
                    PROPERTIES.load(new FileInputStream(PROPERTY_FILE));

                } catch (Exception e) {

                    //if we couldn't find the property file we must be testing in our IDE
                    PROPERTIES.load(Properties.class.getClassLoader().getResourceAsStream(PROPERTY_FILE));

                }
            }

            //return our property
            return PROPERTIES.getProperty(key);

        } catch (Exception ex) {

            //display error
            ex.printStackTrace();

            //exit with error
            System.exit(1);
        }

        return null;
    }
}