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
  public int rounding = Integer.MAX_VALUE;
  private long randSeed;

  public Config(String configfile, String graphFile, String outputDir) {
    Properties prop = new Properties();
    try {
      InputStream input = new FileInputStream(configfile);
      prop.load(input); // load a properties file
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    // get the property values
    this.graphFile =  graphFile; //prop.getProperty("graphFile");
    this.outputDir = outputDir; // prop.getProperty("outputDir");
    this.mcRuns = Integer.parseInt(prop.getProperty("mcRuns", "10000"));
    this.numSeeds = Integer.parseInt(prop.getProperty("numSeeds", "50"));
    setAlgorithm(prop.getProperty("algo", "celf"));
    this.startIter = Integer.parseInt(prop.getProperty("startIter"));
    this.rounding = Integer.parseInt(prop.getProperty("rounding"));
    setPropagationModel(prop.getProperty("model"));
    this.randSeed = Long.parseLong(prop.getProperty("randSeed", "0"));

    if (this.mcRuns <= 0 || this.startIter <= 0) {
      throw new RuntimeException("Values for mcRuns and startIter must be positive");
    }
    if (this.graphFile == null || this.outputDir == null) {
      throw new RuntimeException("Input and output path cannot be null");
    }

    print();
  }

  /**
   * Set which algorithm to use
   */
  private void setAlgorithm(String algo) {
    if (algo.toLowerCase().equals("celf")) {
      algorithm = Algorithm.CELF;
    } else if (algo.toLowerCase().equals("celfpp")) {
      algorithm = Algorithm.CELFPP;
    } else {
      throw new RuntimeException("Algorithm not supported. " + algo);
    }
  }

  /**
   * Set which propagation model to use
   */
  private void setPropagationModel(String model) {
    if (model.toUpperCase().equals("IC")) {
      propagationModel = PropagationModel.IC;
    } else if (model.toUpperCase().equals("LT")) {
      propagationModel = PropagationModel.LT;
    } else {
      throw new RuntimeException("Propgation model not supported. " + model);
    }
  }

  /**
   * Return seed to the random number generator
   */
  public long getRandSeed() {
    return this.randSeed;
  }

  /**
   * Print the parameters for this exec
   */
  public void print() {
    LOGGER.info("Graph file path: " + this.graphFile);
    LOGGER.info("Output location " + this.outputDir);
    LOGGER.info("Algorithm choice: " + this.algorithm);
    LOGGER.info("Number of MC runs: " + this.mcRuns);
    LOGGER.info("Number of seeds " + this.numSeeds);
    LOGGER.info("Propagation model " + this.propagationModel.toString());
    LOGGER.info("Random number seed" + this.randSeed);
  }

  /**
   * @return name of log file
   */
  public String getLogFileName() {
    StringBuilder sb = new StringBuilder();
    sb.append(outputDir).append("/"); // put the log file under the output path

    // assume the grpah file path is sth like: ./java/graph/hep_WC.inf, and we extract the "hep_WC" part
    sb.append(graphFile.substring(graphFile.lastIndexOf("/") + 1, graphFile.lastIndexOf("."))).append("_");
    sb.append(algorithm.toString()).append("_");
    sb.append(mcRuns).append("_");
    sb.append(numSeeds).append("_");
    sb.append(randSeed).append("_");
    sb.append(System.currentTimeMillis()).append(".log");

    return sb.toString();
  }
}
