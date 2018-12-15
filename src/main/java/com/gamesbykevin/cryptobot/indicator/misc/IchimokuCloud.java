package com.gamesbykevin.cryptobot.indicator.misc;

import com.gamesbykevin.cryptobot.candle.Candle;
import com.gamesbykevin.cryptobot.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public class IchimokuCloud extends Indicator {

    //the periods of each calculation may vary
    private final int periodsConversionLine;
    private final int periodsBaseLine;
    private final int periodsLeadingSpanB;
    private final int periodsLaggingSpan;

    //list of our values
    private List<Double> conversionLine;
    private List<Double> baseLine;
    private List<Double> leadingSpanA;
    private List<Double> leadingSpanB;
    private List<Double> laggingSpan;

    public IchimokuCloud(int periodsConversionLine, int periodsBaseLine, int periodsLeadingSpanB, int periodsLaggingSpan) {

        //call parent
        super(Key.IchimokuCloud, 0, null);

        //store our settings
        this.periodsConversionLine = periodsConversionLine;
        this.periodsBaseLine = periodsBaseLine;
        this.periodsLeadingSpanB = periodsLeadingSpanB;
        this.periodsLaggingSpan = periodsLaggingSpan;
    }

    @Override
    public void calculate(List<Candle> candles) {

        //clear values
        getValues().clear();
        getConversionLine().clear();
        getBaseLine().clear();
        getLeadingSpanA().clear();
        getLeadingSpanB().clear();
        getLaggingSpan().clear();

        //perform our calculations
        for (int index = 0; index < candles.size(); index++) {

            //we can't continue until we have enough data
            if (index < periodsConversionLine || index < periodsBaseLine || index < periodsLeadingSpanB || index < periodsLaggingSpan)
                continue;

            double high = 0;
            double low = 0;

            //calculate the conversion line (Tenkan-sen)
            high = identifyValue(candles, index - periodsConversionLine + 1, index, true);
            low = identifyValue(candles, index - periodsConversionLine + 1, index, false);
            double conversionLine = ((high + low) / 2.0d);

            //calculate the base line (Kijun-sen)
            high = identifyValue(candles, index - periodsBaseLine + 1, index, true);
            low = identifyValue(candles, index - periodsBaseLine + 1, index, false);
            double baseLine = ((high + low) / 2.0d);

            //calculate leading span a (Senkou Span A)
            double leadingSpanA = ((conversionLine + baseLine) / 2.0d);

            //calculate leading span b (Senkou Span B)
            high = identifyValue(candles, index - periodsLeadingSpanB + 1, index, true);
            low = identifyValue(candles, index - periodsLeadingSpanB + 1, index, false);
            double leadingSpanB = ((high + low) / 2.0d);

            //calculate lagging span if we have enough data (Chikou Span)
            double laggingSpan = (index + periodsLaggingSpan < candles.size()) ? (candles.get(index + periodsLaggingSpan).getClose()) : 0;

            //add the values to our lists
            getConversionLine().add(conversionLine);
            getBaseLine().add(baseLine);
            getLeadingSpanA().add(leadingSpanA);
            getLeadingSpanB().add(leadingSpanB);

            //only add lagging span if we were able to calculate it
            if (laggingSpan > 0)
                getLaggingSpan().add(laggingSpan);
        }
    }

    private double identifyValue(List<Candle> candles, int start, int end, boolean high) {

        double result = (high) ? candles.get(start).getHigh() : candles.get(start).getLow();

        for (int index = start + 1; index <= end; index++) {

            //are we looking for the high or low
            if (high) {

                //we want the highest value
                if (candles.get(index).getHigh() > result)
                    result = candles.get(index).getHigh();

            } else {

                //we want the lowest value
                if (candles.get(index).getLow() < result)
                    result = candles.get(index).getLow();

            }
        }

        //return our result
        return result;
    }

    public List<Double> getBaseLine() {

        if (this.baseLine == null)
            this.baseLine = new ArrayList<>();

        return this.baseLine;
    }

    public List<Double> getConversionLine() {

        if (this.conversionLine == null)
            this.conversionLine = new ArrayList<>();

        return this.conversionLine;
    }

    public List<Double> getLaggingSpan() {

        if (this.laggingSpan == null)
            this.laggingSpan = new ArrayList<>();

        return this.laggingSpan;
    }

    public List<Double> getLeadingSpanA() {

        if (this.leadingSpanA == null)
            this.leadingSpanA = new ArrayList<>();

        return this.leadingSpanA;
    }

    public List<Double> getLeadingSpanB() {

        if (this.leadingSpanB == null)
            this.leadingSpanB = new ArrayList<>();

        return this.leadingSpanB;
    }

    @Override
    public void display() {
        display("Conversion Line:", getConversionLine());
        display("Base Line:      ", getBaseLine());
        display("Leading Span A: ", getLeadingSpanA());
        display("Leading Span B: ", getLeadingSpanB());
        display("Lagging Span:   ", getLaggingSpan());
    }
}