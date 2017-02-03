package ca.ubc;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Config class saves all config paramters.
 * TODO: Ideally, all attributes of this class should be final, unless we want to override them in a subclass.
 */
public class Config {
  private static final Logger LOGGER = Logger.getLogger(Main.class.getCanonicalName());

  String graphFile;
  public int numSeeds;
  public int mcRuns;
  public PropagationModel propagationModel = PropagationModel.IC;
  public Algorithm algorithm;
  public String outputDir;

  public Config(String fileName) {
    Properties prop = new Properties();
    try {
      InputStream input = new FileInputStream(fileName);
      prop.load(input); // load a properties file
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    // get the property values
    graphFile = prop.getProperty("graphFile");
    mcRuns = Integer.parseInt(prop.getProperty("mcRuns", "10000"));
    numSeeds = Integer.parseInt(prop.getProperty("numSeeds", "50"));
    setAlgorithm(prop.getProperty("algo", "celf"));
    outputDir = prop.getProperty("outputDir");

    if (mcRuns <= 0) {
      throw new RuntimeException("mcRuns must be positive");
    }
    if (graphFile == null || outputDir == null) {
      throw new RuntimeException("input and output path cannot be null");
    }

    print();
  }

  private void setAlgorithm(String algo) {
    if (algo.toLowerCase().equals("celf")) {
      algorithm = Algorithm.CELF;
    } else if (algo.toLowerCase().equals("celfpp")) {
      algorithm = Algorithm.CELFPP;
    } else {
      throw new RuntimeException("Algorithm not supported. " + algo);
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