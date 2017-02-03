package ca.ubc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * Main class
 */
public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getCanonicalName());

  public static void main(String[] args) throws IOException {

    Properties prop = new Properties();
    InputStream input = new FileInputStream("./java/properties/config.properties");
    prop.load(input); // load a properties file

    // get the property values
    final String graphFile = prop.getProperty("graphFile");
    final int mcRuns = Integer.parseInt(prop.getProperty("mcRuns"));
    final int algo = Integer.parseInt(prop.getProperty("algo"));
    final String outputDir = prop.getProperty("outputDir");
    LOGGER.info("Graph file path: " + graphFile);
    LOGGER.info("Number of MC runs: " + mcRuns);
    LOGGER.info("Algorithm choice: " + algo);
    LOGGER.info("Output location " + outputDir);

    if (mcRuns <= 0) {
      throw new RuntimeException("mcRuns must be positive");
    }

    // TODO code that measures running time
    run(graphFile, mcRuns, algo, outputDir);
  }

  /**
   * Run an influence maximization algorithm
   * @param graphFile Input path
   * @param mcRuns number of mc simulations
   * @param algo algorithm selector
   * @param outputDir Output directory name
   */
  private static void run(String graphFile, int mcRuns, int algo, String outputDir) {
    if (algo == 0) {
      // CELF
      LOGGER.info("Running CELF");
      // TODO call CELF running code
    } else if (algo == 1) {
      // CELF++
      LOGGER.info("Running CELF++");
      // TODO call CELF++ running code
    } else {
      LOGGER.warning("Invalid algorithm input, program exits");
      System.exit(-1);
    }
  }

}
