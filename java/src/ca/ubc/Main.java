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
    Config config = new Config();
    InputStream input = new FileInputStream("./java/properties/config.properties");
    prop.load(input); // load a properties file

    // get the property values
    config.graphFile = prop.getProperty("graphFile");
    config.mcRuns = Integer.parseInt(prop.getProperty("mcRuns"));
    config.setAlgorithm(prop.getProperty("algo"));

    config.outputDir = prop.getProperty("outputDir");
    config.print();

    if (config.mcRuns <= 0) {
      throw new RuntimeException("mcRuns must be positive");
    }

    // TODO code that measures running time
    run(config);
  }

  /**
   * Run an influence maximization algorithm
   */
  private static void run(Config config) {
    if (config.algorithm == Algorithm.CELF) {
      // CELF
      LOGGER.info("Running CELF");
      // TODO call CELF running code
    } else if (config.algorithm == Algorithm.CELFPP) {
      // CELF++
      LOGGER.info("Running CELF++");
      // TODO call CELF++ running code
    } else {
      LOGGER.warning("Invalid algorithm input, program exits");
      System.exit(-1);
    }
  }

}
