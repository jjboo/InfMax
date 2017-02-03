package ca.ubc.util;

import ca.ubc.InfluenceMaximization;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Config class saves all config paramters.
 * TODO: Ideally, all attributes of this class should be final, unless we want to override them in a subclass.
 */
public class Config {

  public static enum PropagationModel {
    IC, LT
  }

  public static enum Algorithm {
    CELF, CELFPP
  }

  private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());

  public String graphFile;
  public int numSeeds;
  public int mcRuns;
  public PropagationModel propagationModel = PropagationModel.IC;
  public Algorithm algorithm;
  public String outputDir;
  public int startIter;

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
    startIter = Integer.parseInt(prop.getProperty("startIter"));

    if (mcRuns <= 0 || startIter <= 0) {
      throw new RuntimeException("Values for mcRuns and startIter must be positive");
    }
    if (graphFile == null || outputDir == null) {
      throw new RuntimeException("Input and output path cannot be null");
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
