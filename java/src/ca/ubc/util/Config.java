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
    CELF("celf"),
    CELFPP("celfplus");

    private final String name;

    Algorithm(String s) {
      name = s;
    }

    public String toString() {
      return this.name;
    }
  }

  private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());

  public String graphFile;
  public int numSeeds;
  public int mcRuns;
  public PropagationModel propagationModel;
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
    setPropagationModel(prop.getProperty("model"));

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

  private void setPropagationModel(String model) {
    if (model.toUpperCase().equals("IC")) {
      propagationModel = PropagationModel.IC;
    } else if (model.toUpperCase().equals("LT")) {
      propagationModel = PropagationModel.LT;
    } else {
      throw new RuntimeException("Propgation model not supported. " + model);
    }
  }

  public void print() {
    LOGGER.info("Graph file path: " + graphFile);
    LOGGER.info("Number of MC runs: " + mcRuns);
    LOGGER.info("Algorithm choice: " + algorithm);
    LOGGER.info("Output location " + outputDir);
  }

  /**
   * @return name of log file
   */
  public String getLogFileName() {
    StringBuilder sb = new StringBuilder("LOG_");
    // assume the grpah file path is sth like: ./java/graph/hep_WC.inf, and we extract the "hep_WC" part
    sb.append(graphFile.substring(graphFile.lastIndexOf("/") + 1, graphFile.lastIndexOf("."))).append("_");
    sb.append(algorithm.toString()).append("_");
    sb.append(mcRuns).append("_");
    sb.append(numSeeds).append("_");
    sb.append(System.currentTimeMillis()).append(".log");
    return sb.toString();
  }
}
