package ca.ubc.algo;

import ca.ubc.model.IndependentCascade;
import ca.ubc.util.CelfNode;
import ca.ubc.util.CelfPlusNode;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import ca.ubc.util.InfMaxUtils;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Celf++ algorithm
 */
public class CelfPlusAlgo {
  private static final Logger LOGGER = Logger.getLogger(CelfPlusAlgo.class.getCanonicalName());

  private PriorityQueue<CelfPlusNode> _covQueue;
  private Graph _graph;
  private Config _config;
  private Set<Integer> _seedSet;

  private static final double INITIAL_MG = 0.0;
  private static final int INITIAL_FLAG = 0;
  private static final int NULL_ID = -2;

  /**
   * Constructor
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
    int lastSeedId = NULL_ID; // id of the last chosen seed
    int curBestId = NULL_ID;  // id of the current highest MG node in the priority queue

    // first iteration
    for (int nodeId = 0; nodeId < _graph.n; ++nodeId) {
      CelfPlusNode node = new CelfPlusNode(nodeId, INITIAL_MG, INITIAL_MG, INITIAL_FLAG, curBestId);
      node.mg = IndependentCascade.estimateSpread(_graph, _config, _seedSet, node.id);
      _covQueue.add(node);
    }

    // Select k seeds
    while(_seedSet.size() < _config.numSeeds) {
      CelfPlusNode bestNode = _covQueue.peek();

      // flag is current seed set size
      // means that the MG of bestNode is already up-to-date
      // add this node as seed
      if (bestNode.flag == _seedSet.size()) {
        _seedSet.add(bestNode.id);
        totalSpread += bestNode.mg;
        InfMaxUtils.logSeed(_seedSet.size(), bestNode.id, bestNode.mg, totalSpread, LOGGER);
        lastSeedId = bestNode.id;
        curBestId = NULL_ID;
        _covQueue.poll();

      } else {
        CelfPlusNode newNode; // wait to be MG updated and re-heapifying

        // TODO: The IF-condition is never true in iteration 2, as no CELF++ in 1st iteration, can we do sth better here?
        if (bestNode.prevBest == lastSeedId && bestNode.flag == _seedSet.size() - 1) {
          // u.mg2 = u.mg1; skip MG re-computation
          // TODO Check: Should the new mg2 be 0 or be left alone
          newNode = new CelfPlusNode(bestNode.id, bestNode.mg2, 0, _seedSet.size(), bestNode.prevBest);
        } else {
          // need to do MG recomputation
          double[] spreads = IndependentCascade.estimateSpreadPlus(_graph, _config, _seedSet,
              bestNode.id, bestNode.prevBest, true);
          newNode = new CelfPlusNode(bestNode.id, spreads[0] - totalSpread, spreads[1] - totalSpread,
              _seedSet.size(), curBestId);
        }

        // Re-heapify
        _covQueue.poll();
        _covQueue.add(newNode);
        // Update current Best
        curBestId = _covQueue.peek().id;
      }
    }

    return totalSpread;
  }
}
