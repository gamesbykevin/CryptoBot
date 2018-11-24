package com.gamesbykevin.cryptobot.history;

import lombok.extern.log4j.Log4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static com.gamesbykevin.cryptobot.util.Util.DELIMITER;

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
     * Print update loading our history periodically
     */
    private static final int LIMIT = 10000;

    public static void load(History history, String directory) throws Exception {

        //get the home directory;
        File folder = new File(directory);

        //load all of the files in the directory
        for (File file : folder.listFiles()) {

            //start reading the file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            //count how many candles we have loaded
            int count =  0;

            //check every line of the text file
            while(true) {

                if (count % LIMIT == 0)
                    log.info(directory + " - " + file.getName() + " - count: " + count);

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
    }
}