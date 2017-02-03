package ca.ubc;

import ca.ubc.algo.CelfAlgo;
import ca.ubc.algo.CelfPlusAlgo;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * main class for running
 */
public class InfluenceMaximization {

  private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());

  public static void main(String[] args) throws IOException {

    String fileName = "./java/properties/config.properties";
    Config config = new Config(fileName);

    // TODO: Add code that measures running time
    run(config);
  }

  /**
   * Run an influence maximization algorithm
   */
  private static void run(Config config) {
    Graph graph = new Graph(config.graphFile);
    if (config.algorithm == Config.Algorithm.CELF) {
      LOGGER.info("Running CELF");
      CelfAlgo celfAlgo = new CelfAlgo(graph, config);
      celfAlgo.run();
    } else if (config.algorithm == Config.Algorithm.CELFPP) {
      LOGGER.info("Running CELF++");
      CelfPlusAlgo celfPlusAlgo = new CelfPlusAlgo(graph, config);
      celfPlusAlgo.run();
    } else {
      LOGGER.warning("Invalid algorithm input, program exits");
      System.exit(-1);
    }
  }
}
