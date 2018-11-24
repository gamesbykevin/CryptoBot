package com.gamesbykevin.cryptobot.history;

import com.gamesbykevin.cryptobot.candle.Candle;
import lombok.extern.log4j.Log4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import static com.gamesbykevin.cryptobot.util.Util.DELIMITER;
import static com.gamesbykevin.cryptobot.util.Util.getPrintWriter;

@Log4j
public class HistoryHelper {

    /**
     * When we load / save market data we need to know where it is at
     */
    private static final int INDEX_TIME = 0;
    private static final int INDEX_LOW = 1;
    private static final int INDEX_HIGH = 2;
    private static final int INDEX_OPEN = 3;
    private static final int INDEX_CLOSE = 4;
    private static final int INDEX_VOLUME = 5;

    /**
     * Print update loading our history every so many records
     */
    private static final int UPDATE_LIMIT = 10000;

    /**
     * When writing market data to .txt files we want to limit the size
     */
    private static final int FILE_LIMIT = 50000;

    /**
     * What do we start our file name with
     */
    private static final String FILE_NAME_PREFIX = "candles";

    /**
     * Our files will be text files
     */
    private static final String FILE_NAME_SUFFIX = ".txt";

    /**
     * Parent directory where all market data will be stored
     */
    public static final String PARENT_DIRECTORY = "market_data";

    public static void load(History history, String directory) throws Exception {

        //get the home directory;
        File folder = new File(directory);

        //get list of all the files in our directory
        File[] files = folder.listFiles();

        //don't continue if there are no files
        if (files == null) {
            log.info("No files found to load market data: " + directory);
            return;
        }

        //load all of the files in the directory
        for (File file : files) {

            //start reading the file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            //count how many candles we have loaded
            int count =  0;

            //check every line of the text file
            while(true) {

                //display progress update
                if (count % UPDATE_LIMIT == 0)
                    log.info("Loading \"" + directory + "\" - " + file.getName() + " - count: " + count);

                //read the line in the text file
                final String line = bufferedReader.readLine();

                //if null we are done reading our file
                if (line == null)
                    break;

                //the line is a json string we can convert to array
                String[] tmpData = line.split(DELIMITER);

                //parse our data
                double open = Double.parseDouble(tmpData[INDEX_OPEN]);
                double close = Double.parseDouble(tmpData[INDEX_CLOSE]);
                double high = Double.parseDouble(tmpData[INDEX_HIGH]);
                double low = Double.parseDouble(tmpData[INDEX_LOW]);
                double volume = Double.parseDouble(tmpData[INDEX_VOLUME]);
                long time = Long.parseLong(tmpData[INDEX_TIME]);

                //add period to our history
                history.add(open, close, high, low, volume, time);

                //add to our count
                count++;
            }

            //recycle
            bufferedReader.close();
            bufferedReader = null;
            file = null;
        }

        //now that all data is loaded, let's sort
        history.sort();
    }

    public static void write(History history, String directory) {

        //order the market data
        history.sort();

        //create our print writer
        PrintWriter pw = null;

        //keep track of how many records we write to a file
        int count = FILE_LIMIT;

        //how many files have we created
        int files = 0;

        //notify action
        log.info("Writing market data to .txt file(s) - " + directory);

        //write all data to files
        for (int index = 0; index < history.getCandles().size(); index++) {

            //keep track of count
            count++;

            //if we reached our file limit let's create a new file
            if (count > FILE_LIMIT) {

                //if the object exists let's cleanup
                if (pw != null) {
                    pw.flush();
                    pw.close();
                }

                //create a new print writer
                pw = getPrintWriter(FILE_NAME_PREFIX + "_" + files + FILE_NAME_SUFFIX, directory);

                //keep track of how many files we have created
                files++;

                //reset file count
                count = 0;
            }

            //get the current period
            Candle candle = history.getCandles().get(index);

            //write data to file in this order
            pw.println(
                candle.getTime() + DELIMITER +
                candle.getLow() + DELIMITER +
                candle.getHigh() + DELIMITER +
                candle.getOpen() + DELIMITER +
                candle.getClose() + DELIMITER +
                candle.getVolume()
            );
        }

        //if the object exists let's cleanup
        if (pw != null) {
            pw.flush();
            pw.close();
        }

        //flag null for garbage collection
        pw = null;
    }
}