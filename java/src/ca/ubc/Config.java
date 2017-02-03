package ca.ubc;

import java.util.logging.Logger;

public class Config {
  private static final Logger LOGGER = Logger.getLogger(Main.class.getCanonicalName());

  String graphFile;
  public int numSeeds;
  public int mcRuns;
  public PropagationModel propagationModelModel;
  public Algorithm algorithm;
  public String outputDir;

  public void setAlgorithm(String algo) {
    if (algo.toLowerCase().equals("celf")) {
      algorithm = Algorithm.CELF;
    } else if (algo.toLowerCase().equals("celfpp")) {
      algorithm = Algorithm.CELFPP;
    } else {
      System.out.println("Algorithm not supported. " + algo);
    }
  }

  public void print() {
    LOGGER.info("Graph file path: " + graphFile);
    LOGGER.info("Number of MC runs: " + mcRuns);
    LOGGER.info("Algorithm choice: " + algorithm);
    LOGGER.info("Output location " + outputDir);
  }
}

enum PropagationModel {
  IC, LT
}

enum Algorithm {
  CELF, CELFPP
}