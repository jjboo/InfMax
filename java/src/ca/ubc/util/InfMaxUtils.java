package ca.ubc.util;

import java.util.logging.Logger;


/**
 * Misc utils methods
 */
public class InfMaxUtils {

  private static final String TAB = "\t";

  /**
   * log seed selection
   * Format of printing: seedCount \t seedNodeId \t MG \t totalSpread
   */
  public static void logSeed(Logger logger, int cnt, int id, double mg, double totalSpread, double timeSpentInMin) {
    StringBuilder sb = new StringBuilder();
    sb.append(cnt);
    sb.append(TAB);
    sb.append(id);
    sb.append(TAB);
    sb.append(mg);
    sb.append(TAB);
    sb.append(totalSpread);
    sb.append(TAB);
    sb.append(timeSpentInMin);
    logger.info(sb.toString());
  }

  // running time in minutes
  public static double runningTimeMin(long startTime) {
    long curTime = System.currentTimeMillis();
    double durationInMin = (double)(curTime - startTime) / (1000.0 * 60.0);
    return durationInMin;
  }
}
