package com.gamesbykevin.cryptobot.util;

public class Properties {

    private static java.util.Properties PROPERTIES;

    //location of property file
    public static final String PROPERTY_FILE = "./config.properties";

    public static String getProperty(String key) {

        try {

            if (PROPERTIES == null) {
                PROPERTIES = new java.util.Properties();
                PROPERTIES.load(Properties.class.getClassLoader().getResourceAsStream(PROPERTY_FILE));
            }

            return PROPERTIES.getProperty(key);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}