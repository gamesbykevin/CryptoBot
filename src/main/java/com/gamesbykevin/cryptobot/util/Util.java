package com.gamesbykevin.cryptobot.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    /**
     * When purchasing stock we round the quantity to make it simple
     */
    public static final int ROUND_DECIMALS_QUANTITY = 2;

    /**
     * When printing text and we want to start a new line
     */
    public static final String NEW_LINE = "\n";

    /**
     * Use this character to separate file directories
     */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * The market data in our .txt file will be separated by this
     */
    public static final String DELIMITER = ",";

    public static Map<String, String> parseUrl(String link) throws Exception {

        //create our url
        URL url = new URL(link);

        //this is where we will store our parameters
        Map<String, String> query_pairs = new LinkedHashMap<>();

        //get the query string parameters
        String query = url.getQuery();

        //separate each parameter
        String[] pairs = query.split("&");

        //add each one to our list
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }

        //return our hashmap
        return query_pairs;
    }

    public static BigDecimal round(int decimals, BigDecimal value) {
        return value.setScale(decimals, RoundingMode.HALF_DOWN);
    }
}