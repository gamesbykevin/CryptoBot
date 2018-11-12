package com.gamesbykevin.cryptobot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    public static BigDecimal round(int decimals, BigDecimal value) {
        return value.setScale(decimals, RoundingMode.HALF_DOWN);
    }
}