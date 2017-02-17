package ca.ubc.algo;

import ca.ubc.model.IndependentCascade;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Evaluating influence spread for a given seed set
 */
public class EvaluateInfluenceSpread {

  //private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());
  private Config _config;
  private Graph _graph;
  private Set<Integer> _seedSet;
  private IndependentCascade _cascade;

  /**
   * public constructor
   */
  public EvaluateInfluenceSpread(Graph graph, Config config) {
    _graph = graph;
    _config = config;
    _seedSet = new HashSet<>();
    _cascade = new IndependentCascade(config.getRandSeed());
  }

  /**
   * Run function
   */
  public void run() throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(_config.getSeedFileName()));
    String line;
    while ((line = br.readLine()) != null) {
      int seed = Integer.parseInt(line);
      _seedSet.add(seed);
    }
    br.close();

    double[] stats = _cascade.spreadStats(_graph, _config, _seedSet);
    System.out.println("spread = " + stats[0]);
    System.out.println("mean = " + stats[1]);
    System.out.println("std dev = " + stats[2]);
  }
}
