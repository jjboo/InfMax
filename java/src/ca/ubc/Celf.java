package ca.ubc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Celf
 */
public class Celf {

  private static final Logger LOGGER = Logger.getLogger(Celf.class.getCanonicalName());

  private PriorityQueue<CelfNode> _covQueue;
  private Graph _graph;
  private Config _config;
  private Random _random;
  private Set<Integer> _seedSet;
  //private int[] _finalSeeds;
  //private double[] _finalGains;

  /**
   * Constructor
   */
  public Celf(Graph graph, Config config) {
    _graph = graph;
    _config = config;
    _covQueue = new PriorityQueue<>(new NodeComparator());
    _random = new Random();
    _seedSet = new HashSet<>();

    //_finalGains = new double[_config.numSeeds];
    //_finalSeeds = new int[_config.numSeeds];
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
      _covQueue.add(new CelfNode(i, computeSpreadIC(_seedSet, i), 0));
    }

    // get the first seed
    CelfNode topNode = _covQueue.poll();
    _seedSet.add(topNode.id);
    totalSpread += topNode.mg;
    logSeed(_seedSet.size(), topNode.id, topNode.mg, totalSpread);

    // get seeds no. 2 to k
    while (_seedSet.size() < _config.numSeeds) {
      CelfNode bestNode = _covQueue.peek();

      if (bestNode.flag == _seedSet.size()) {
        // flag is current seed set size
        // means that the MG of bestNode is already up-to-date
        // add this node as seed
        _seedSet.add(bestNode.id);
        totalSpread += bestNode.mg;
        logSeed(_seedSet.size(), bestNode.id, bestNode.mg, totalSpread);
        _covQueue.poll();

      } else {
        // re-compute MG
        double newMg = computeSpreadIC(_seedSet, bestNode.id) - totalSpread;
        // update flag and re-heapify
        int id = bestNode.id;
        _covQueue.poll();
        _covQueue.add(new CelfNode(id, newMg, _seedSet.size()));
      }
    }

    return totalSpread;
  }

  /**
   * Given a seed set, compute spread under IC model
   */
  private double computeSpreadIC(Set<Integer> seeds, Integer candidate) {
    boolean[] active = new boolean[_graph.n];
    Queue<Integer> bfsQueue = new LinkedList<>();
    double ret = 0;

    for (int i = 0; i < _config.mcRuns; ++i) {
      Arrays.fill(active, false);
      bfsQueue.clear();
      int countActive = 0;

      if (candidate != null) {
        // activate the candidate
        active[candidate] = true;
        bfsQueue.add(candidate);
        countActive++;
      }

      // activate all seeds
      for (int s : seeds) {
        countActive++;
        active[s] = true;
        bfsQueue.add(s);
      }

      while (!bfsQueue.isEmpty()) {
        int w = bfsQueue.poll(); // this also removes w from BFS queue
        List<Integer> neighborList = _graph.neighbors[w];
        List<Double> probList = _graph.probs[w];

        // try to activate neighbours of w
        for (int j = 0; j < neighborList.size(); ++j) {
          int v = neighborList.get(j);
          // only look at inactive out-neighbours
          if (!active[v]) {
            double prob = probList.get(j);
            if (_random.nextDouble() <= prob) {
              active[v] = true;
              countActive++;
              bfsQueue.add(v);
            }
          }
        }
      }

      ret += (1.0 * countActive) / (1.0 * _config.mcRuns);
    }

    return ret;
  }

  /**
   * log seed selection
   */
  private void logSeed(int cnt, int id, double mg, double totalSpread) {
    StringBuilder sb = new StringBuilder();
    sb.append(cnt);
    sb.append(", ");
    sb.append(id);
    sb.append(", ");
    sb.append(mg);
    sb.append(", ");
    sb.append(totalSpread);
    LOGGER.info(sb.toString());
  }
}
