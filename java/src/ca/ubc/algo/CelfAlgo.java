package ca.ubc.algo;

import ca.ubc.model.IndependentCascade;
import ca.ubc.util.CelfNode;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import ca.ubc.util.InfMaxUtils;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Celf
 */
public class CelfAlgo {

  private static final Logger LOGGER = Logger.getLogger(CelfAlgo.class.getCanonicalName());

  private PriorityQueue<CelfNode> _covQueue;
  private Graph _graph;
  private Config _config;
  private Set<Integer> _seedSet;

  private static final int INITIAL_FLAG = 0;

  /**
   * Constructor
   */
  public CelfAlgo(Graph graph, Config config) {
    _graph = graph;
    _config = config;
    _covQueue = new PriorityQueue<>(new CelfNode.NodeComparator());
    _seedSet = new HashSet<>();
  }

  /**
   * Mine seeds
   * @return total spread of the chosen seed set
   */
  public double run() {
    long startTime = System.currentTimeMillis();

    double totalSpread = 0;
    _seedSet.clear();

    // first iteration
    for (int nodeId = 0; nodeId < _graph.n; ++nodeId) {
      _covQueue.add(new CelfNode(nodeId, IndependentCascade.estimateSpread(_graph, _config, _seedSet, nodeId), INITIAL_FLAG));
    }

    // Select k seeds
    while (_seedSet.size() < _config.numSeeds) {
      CelfNode bestNode = _covQueue.peek();

      // flag is current seed set size
      // means that the MG of bestNode is already up-to-date
      // add this node as seed
      if (bestNode.flag == _seedSet.size()) {
        _seedSet.add(bestNode.id);
        totalSpread += bestNode.mg;
        InfMaxUtils.logSeed(LOGGER, _seedSet.size(), bestNode.id, bestNode.mg, totalSpread, InfMaxUtils.runningTimeMin(startTime));
        _covQueue.poll();

      } else {
        // re-compute MG
        double newMg = IndependentCascade.estimateSpread(_graph, _config, _seedSet, bestNode.id) - totalSpread;
        // update flag and re-heapify
        int id = bestNode.id;
        _covQueue.poll();
        _covQueue.add(new CelfNode(id, newMg, _seedSet.size()));
      }
    }

    return totalSpread;
  }
}
