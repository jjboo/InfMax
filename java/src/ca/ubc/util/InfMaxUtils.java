package ca.ubc.util;

import java.io.BufferedWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Misc utils methods
 */
public class InfMaxUtils {

  private static final String TAB = "\t";

  /**
   * log seed selection
   * Format of printing: seedCount \t seedNodeId \t MG \t totalSpread
   */
  public static String logSeed(Logger logger, int cnt, int id, double mg, double totalSpread,
                             int lookUps, int celfPlusSave, double timeSpentInMin) {
    StringBuilder sb = new StringBuilder();
    sb.append(cnt).append(TAB).append(id).append(TAB)
            .append(String.format("%.5f", mg)).append(TAB)
            .append(String.format("%.5f", totalSpread)).append(TAB)
            .append(lookUps).append(TAB)
            .append(celfPlusSave).append(TAB)
            .append(String.format("%.5f", timeSpentInMin));
    String msg = sb.toString();
    logger.info(msg);
    return msg + "\n";
  }

  /**
   * running time in minutes
   */
  public static double runningTimeMin(long startTime) {
    return (double) (System.currentTimeMillis() - startTime) / (1000.0 * 60.0);
  }

  /**
   * set log file for a specific logger
   */
  public static void setLogFile(Logger logger, Config config) {
    try {
      FileHandler fh = new FileHandler(config.getLogFileName());
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Round the number to "places" decimal points
   */
  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

}
