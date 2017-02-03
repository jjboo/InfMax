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
    double totalSpread = 0;
    _seedSet.clear();

    // first iteration
    for (int i = 0; i < _graph.n; ++i) {
      _covQueue.add(new CelfNode(i, IndependentCascade.estimateSpread(_graph, _config, _seedSet, i), 0));
    }

    // get the first seed
    CelfNode topNode = _covQueue.poll();
    _seedSet.add(topNode.id);
    totalSpread += topNode.mg;
    InfMaxUtils.logSeed(_seedSet.size(), topNode.id, topNode.mg, totalSpread, LOGGER);

    // get seeds no. 2 to k
    while (_seedSet.size() < _config.numSeeds) {
      CelfNode bestNode = _covQueue.peek();

      if (bestNode.flag == _seedSet.size()) {
        // flag is current seed set size
        // means that the MG of bestNode is already up-to-date
        // add this node as seed
        _seedSet.add(bestNode.id);
        totalSpread += bestNode.mg;
        InfMaxUtils.logSeed(_seedSet.size(), bestNode.id, bestNode.mg, totalSpread, LOGGER);
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

//  /**
//   * Given a seed set, compute spread under IC model
//   */
//  public double computeSpreadIC(Set<Integer> seeds, Integer candidate) {
//    boolean[] active = new boolean[_graph.n];
//    Queue<Integer> bfsQueue = new LinkedList<>();
//    double ret = 0;
//
//    for (int i = 0; i < _config.mcRuns; ++i) {
//      Arrays.fill(active, false);
//      bfsQueue.clear();
//      int countActive = 0;
//
//      if (candidate != null) {
//        // activate the candidate
//        active[candidate] = true;
//        bfsQueue.add(candidate);
//        countActive++;
//      }
//
//      // activate all seeds
//      for (int s : seeds) {
//        countActive++;
//        active[s] = true;
//        bfsQueue.add(s);
//      }
//
//      while (!bfsQueue.isEmpty()) {
//        int w = bfsQueue.poll(); // this also removes w from BFS queue
//        List<Integer> neighborList = _graph.neighbors[w];
//        List<Double> probList = _graph.probs[w];
//
//        // try to activate neighbours of w
//        for (int j = 0; j < neighborList.size(); ++j) {
//          int v = neighborList.get(j);
//          // only look at inactive out-neighbours
//          if (!active[v]) {
//            double prob = probList.get(j);
//            if (_random.nextDouble() <= prob) {
//              active[v] = true;
//              countActive++;
//              bfsQueue.add(v);
//            }
//          }
//        }
//      }
//
//      ret += (1.0 * countActive) / (1.0 * _config.mcRuns);
//    }
//
//    return ret;
//  }

}
