package ca.ubc;

import ca.ubc.algo.CelfAlgo;
import ca.ubc.algo.CelfPlusAlgo;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * main class for running
 */
public class InfluenceMaximization {

  private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());

  private static final int NUM_ARGS = 4;

  public static void main(String[] args) throws IOException {
    if (args.length != NUM_ARGS) {
      StringBuilder sb = new StringBuilder();
      sb.append("Usage: java -classpath InfMax.jar ca.ubc.InfluenceMaximization [configFile] [graphFile] [outputDir] [randSeed]\n");
      sb.append("[configFile]: path to a config property file");
      sb.append("[graphFile]: path to the input graph file (dataset)");
      sb.append("[outputDir]: location to store output logs");
      sb.append("[randSeed]: Seed of the random number generator");
      System.out.println(sb.toString());
      System.exit(-1);
    }

    LOGGER.info("Command line arguments: " + args[0] + " " + args[1] + " " + args[2] + " " + args[3]);
    Config config = new Config(args[0], args[1], args[2], args[3]);
    run(config);
    LOGGER.info("Program completed");
  }

  /**
   * Run an influence maximization algorithm
   */
  private static void run(Config config) {
    Graph graph = new Graph(config.graphFile);

    BufferedWriter bw = null;
    FileWriter fw = null;

    try {
      String logFileName = config.getLogFileName();
      fw = new FileWriter(logFileName);
      bw = new BufferedWriter(fw);

      if (config.algorithm == Config.Algorithm.CELF) {
        LOGGER.info("Running CELF");
        CelfAlgo celfAlgo = new CelfAlgo(graph, config, bw);
        celfAlgo.run();
      } else if (config.algorithm == Config.Algorithm.CELFPP) {
        LOGGER.info("Running CELF++");
        CelfPlusAlgo celfPlusAlgo = new CelfPlusAlgo(graph, config, bw);
        celfPlusAlgo.run();
      } else {
        LOGGER.warning("Invalid algorithm input, program exits");
        System.exit(-1);
      }

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    } finally {
      try {
        if (bw != null) {
          bw.close();
        }
        if (fw != null) {
          fw.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }
  }
}
