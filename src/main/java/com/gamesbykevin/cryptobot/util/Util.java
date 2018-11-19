package com.gamesbykevin.cryptobot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    /**
     * When purchasing stock we round the quantity to make it simple
     */
    public static final int ROUND_DECIMALS_QUANTITY = 2;


    /**
     * When printing text and we want to start a new line
     */
    public static final String NEW_LINE = "\n";

    public static BigDecimal round(int decimals, BigDecimal value) {
        return value.setScale(decimals, RoundingMode.HALF_DOWN);
    }
}