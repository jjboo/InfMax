package ca.ubc.algo;

import ca.ubc.model.IndependentCascade;
import ca.ubc.util.CelfNode;
import ca.ubc.util.CelfPlusNode;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Celf++ algorithm
 */
public class CelfPlusAlgo {
  private static final Logger LOGGER = Logger.getLogger(CelfPlusAlgo.class.getCanonicalName());

  private PriorityQueue<CelfNode> _covQueue;
  private Graph _graph;
  private Config _config;
  private Set<Integer> _seedSet;

  private static final double INITIAL_MG = 0.0;
  private static final int INITIAL_FLAG = 0;

  /**
   * constructor
   */
  public CelfPlusAlgo(Graph graph, Config config) {
    _graph = graph;
    _config = config;
    _seedSet = new HashSet<>();
    _covQueue = new PriorityQueue<>(new CelfNode.NodeComparator());
  }

  /**
   * Mine seeds
   */
  public double run() {
    double totalSpread = 0;
    _seedSet.clear();

    // first iteration
    for (int nodeId = 0; nodeId < _graph.n; ++nodeId) {
      CelfPlusNode node = new CelfPlusNode(nodeId, INITIAL_MG, INITIAL_MG, INITIAL_FLAG, -1);
    }

    return totalSpread;
  }

}
