package ca.ubc;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Main class
 */
public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getCanonicalName());

  public static void main(String[] args) throws IOException {

    // TODO: fileName should come from command line argument
    String fileName = "./java/properties/config.properties";
    Config config = new Config(fileName);

    // TODO code that measures running time
    run(config);
  }

  /**
   * Run an influence maximization algorithm
   */
  private static void run(Config config) {
    Graph graph = new Graph(config.graphFile);
    if (config.algorithm == Algorithm.CELF) {
      // CELF
      LOGGER.info("Running CELF");
      Celf celf = new Celf(graph, config);
      celf.run();
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
