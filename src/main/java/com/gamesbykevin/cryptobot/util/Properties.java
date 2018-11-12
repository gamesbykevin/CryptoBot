package com.gamesbykevin.cryptobot.util;

import java.io.FileInputStream;

public class Properties {

    //object used to access our properties
    private static java.util.Properties PROPERTIES;

    //location of property file
    public static final String PROPERTY_FILE = "./config.properties";

    public static String getProperty(String key) {

        try {

            //if null, instantiate and load
            if (PROPERTIES == null) {

                //loading...
                System.out.println("Loading properties: " + PROPERTY_FILE);

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