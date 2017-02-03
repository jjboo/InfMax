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
  public static void logSeed(int cnt, int id, double mg, double totalSpread, Logger logger) {
    StringBuilder sb = new StringBuilder();
    sb.append(cnt);
    sb.append(TAB);
    sb.append(id);
    sb.append(TAB);
    sb.append(mg);
    sb.append(TAB);
    sb.append(totalSpread);
    logger.info(sb.toString());
  }
}
