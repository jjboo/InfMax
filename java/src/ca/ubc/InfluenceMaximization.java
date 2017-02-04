package ca.ubc;

import ca.ubc.algo.CelfAlgo;
import ca.ubc.algo.CelfPlusAlgo;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import ca.ubc.util.InfMaxUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * main class for running
 */
public class InfluenceMaximization {

  private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());

  private static final int NUM_ARGS = 3;

  public static void main(String[] args) throws IOException {
    if (args.length != NUM_ARGS) {
      System.out.println("Usage: java -classpath InfMax.jar ca.ubc.InfluenceMaximization [configFile] [graphFile] [outputDir]");
      System.exit(-1);
    }

    LOGGER.info("Command line arguments: " + args[0] + " " + args[1] + " " + args[2]);
    Config config = new Config(args[0], args[1], args[2]);
    InfMaxUtils.setLogFile(LOGGER, config);
    run(config);
    LOGGER.info("Program completed");
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

  //private static void testRun(Config config) {
  //  Graph graph = new Graph(config.graphFile);
  //  Set<Integer> set  = new HashSet<>();
  //  set.add(1);
  //  set.add(2);
  //  System.out.println(IndependentCascade.estimateSpread(graph, config, set, null));
  //}
}
